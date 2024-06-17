import { useEffect } from 'react';
import { Tab, ListGroup, Button } from 'react-bootstrap'
import { axiosAPI } from '../../../../api/axiosClient';
import { useParams } from 'react-router';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Link } from 'react-router-dom';
import { Card } from 'react-bootstrap';
import CommentsComponent from './common/CommentsComponent';
import { Tabs } from 'react-bootstrap';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { Spinner } from 'react-bootstrap';

const TeacherAssignmentComponent = () => {
    let { id } = useParams();
    const [assignment, setAssignment] = useState('')
    const [studentsAcceptedTask, setAcceptedAssignments] = useState([])
    const navigate = useNavigate();
    const [selectedUser, setSelectedUser] = useState(null);
    const [user, setUser] = useState(null);
    const [editing, setEditing] = useState(true);
    const [form, setForm] = useState({ rating: "" });
    const [validationLoading, setValidationLoading] = useState(false);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const stompClient = Stomp.over(socket);


        stompClient.connect({
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }, (frame) => {
            stompClient.subscribe('/user/topic/openai', (message) => {
                setValidationLoading(false)
                window.open(message.body.substring(1, message.body.length - 1), '_blank', 'noopener,noreferrer').focus()
            });

        });


        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    useEffect(() => {
        const fetchAssignment = async () => {
            axiosAPI.get(`/assignment/${id}`,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                    }
                }
            )
                .then(response => {
                    setAssignment(response.data)
                    setAcceptedAssignments(response.data.studentsAcceptedTask)
                })
                .catch(error => {
                    navigate('/error', {
                        state: {
                            code: error.message,
                            message: error.response.data.messages
                        }
                    })
                })
        };
        fetchAssignment()

        axiosAPI.get('/user/user-info', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        }).then(response => {
            setUser(response.data)
        })
            .catch(error => {
                navigate('/error', {
                    state: {
                        code: error.message,
                        message: error.response.data.messages
                    }
                })
            })
    }, [])

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleTabSelect = (key) => {
        setForm({ rating: '' })
        setEditing(true)
        setSelectedUser(studentsAcceptedTask.filter(elem => elem.user.id == key)[0]);
    };
    const confirmRate = () => {
        axiosAPI.put('/assignment/rate-assignment', null, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            params: {
                assignmentId: assignment.id,
                userId: selectedUser.user.id,
                rating: form.rating
            }
        }).then(response => {
            setEditing(true)
            setSelectedUser(response.data)
            setAcceptedAssignments(prevTasks =>
                prevTasks.map(user =>
                    user.user.id === response.data.user.id ? response.data : user
                )
            )
        })
            .catch(error => {
                navigate('/error', {
                    state: {
                        code: error.message,
                        message: error.response.data.messages
                    }
                })
            })
    }

    const validate = () => {
        axiosAPI.post('/open-ai/validate', null, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            params: {
                repositoryName: selectedUser.repositoryName
            }
        })
            .then(response => {
                console.log()
            })
            .catch(error => {
                navigate('/error', {
                    state: {
                        code: error.message,
                        message: error.response.data.messages
                    }
                })
            })
    }

    return (

        <>
            <div style={{ backgroundColor: 'white', borderBottom: '1px solid', marginBottom: '10px' }}>
                <div style={{ height: '25px', maxWidth: '95%', margin: '0 auto' }}>
                    <span><Link to={assignment.course ? `/owned-courses/${assignment.course.id}` : '/joined-courses'}>{assignment.course ? assignment.course.name : 'Course'}</Link>  </span>
                </div>
            </div>
            <Tabs
                defaultActiveKey="home"
                id="fill-tab-example"
                className="mb-3"
                style={{ width: '30%', margin: '0 auto' }}
                fill
            >
                <Tab eventKey="home" title="Home">
                    <div style={{ width: '70%', margin: '0 auto', paddingTop: '20px', display: 'flex', gap: '0px' }} className="cont">
                        <div style={{ flex: '1 1 auto', minWidth: '100px', padding: '15px' }} >
                            <h2>{assignment.name}</h2>
                            <h5>    Posted at: {assignment.createdAt ? new Date(assignment.createdAt).toLocaleDateString('en', { year: 'numeric', month: 'short', day: '2-digit', }) : "No creation date was found"}</h5>
                            <h5>    Deadline at: {assignment.deadline ? new Date(assignment.deadline).toLocaleDateString('en', { year: 'numeric', month: 'short', day: '2-digit', }) : "No deadline date was found"}</h5>
                            <div style={{ width: '100%', height: '2px', backgroundColor: 'black', margin: '10px auto' }}></div>
                            <h6>{assignment.description}</h6>

                        </div></div>
                </Tab>
                <Tab eventKey="students" title="Students">
                    {assignment ? (
                        <div style={{ paddingLeft: '20px' }}>
                            <Tab.Container id="list-group-tabs-example" onSelect={handleTabSelect} >
                                <div style={{ display: 'flex', height: '100%' }} >
                                    <div style={{ maxHeight: "93vh", overflowY: "auto", flexBasis: '15%', flexShrink: 0 }}>
                                        {studentsAcceptedTask.filter(elem => elem.rating !== null).length > 0 ? <h5>Reviewed tasks</h5> : null}
                                        <ListGroup >
                                            {studentsAcceptedTask.map((elem) => (elem.rating !== null ?
                                                <ListGroup.Item action eventKey={elem.user.id}>
                                                    {elem.user.gitHubUsername}
                                                </ListGroup.Item> : null
                                            ))}
                                        </ListGroup>
                                        {studentsAcceptedTask.filter(elem => elem.status === 'READY' && elem.rating === null).length > 0 ? <h5>Tasks ready for review</h5> : null}
                                        <ListGroup >
                                            {studentsAcceptedTask.map((elem) => (elem.status === 'READY' && elem.rating === null ?
                                                <ListGroup.Item action eventKey={elem.user.id}>
                                                    {elem.user.gitHubUsername}
                                                </ListGroup.Item> : null
                                            ))}
                                        </ListGroup>
                                        {studentsAcceptedTask.filter(elem => elem.status !== 'READY' && elem.rating === null).length > 0 ? <h5>Tasks not ready for review</h5> : null}
                                        <ListGroup >
                                            {studentsAcceptedTask.map((elem) => (elem.status !== 'READY' && elem.rating === null ?
                                                <ListGroup.Item action eventKey={elem.user.id}>
                                                    {elem.user.gitHubUsername}
                                                </ListGroup.Item> : null
                                            ))}
                                        </ListGroup>
                                    </div>
                                    <div style={{ flexBasis: '85%', paddingLeft: '10px', height: '500px' }}>
                                        {selectedUser ? <>
                                            <Card className="text-center" style={{ maxWidth: '400px', margin: '0 auto' }}>
                                                <Card.Body>
                                                    <Card.Title>Assignment repository</Card.Title>
                                                    <Card.Subtitle><a href={`https://github.com/ClassroomSleek/${selectedUser.repositoryName}`}>{selectedUser.repositoryName}</a> </Card.Subtitle>
                                                    <Card.Text>{editing ? (selectedUser && selectedUser.rating ? `${selectedUser.rating}/` : '') : <><input style={{ width: '45px' }} value={form.rating} name="rating" onChange={handleChange} />/</>}{assignment.rating} points</Card.Text>
                                                </Card.Body>
                                            </Card>
                                            <div style={{ marginLeft: "62%", marginTop: '2%', display: 'flex', gap: '10px', flexDirection: editing ? "column" : "row" }}>
                                                {editing ?
                                                    <>
                                                        <div>
                                                            <Button variant='success' style={{ width: "100px", height: "40px" }} onClick={() => setEditing(false)}>Rate</Button>
                                                        </div>
                                                        <div>
                                                            {validationLoading ?
                                                                <Spinner animation="border" role="status" style={{ marginLeft: "35px" }}>
                                                                    <span className="visually-hidden">Loading...</span>
                                                                </Spinner>
                                                                : <Button style={{ width: "100px", height: "40px" }} onClick={() => {validate(); setValidationLoading(true)}}>Validate</Button>}

                                                        </div>
                                                    </>
                                                    :
                                                    <>
                                                        <Button variant='success' onClick={confirmRate}>Cofirm</Button>
                                                        <Button variant='danger' onClick={() => { setEditing(true); setForm({ rating: '' }) }}>Cancel</Button>
                                                    </>
                                                }
                                            </div>
                                            <div style={{ maxWidth: '800px', margin: '0 auto' }}>
                                                <CommentsComponent repositoryName={selectedUser.repositoryName} user={user} />
                                            </div>
                                        </> : ""}
                                    </div>
                                </div>
                            </Tab.Container>
                        </div>
                    ) : null
                    }                </Tab>
            </Tabs>

        </>

    );
}

export default TeacherAssignmentComponent;

