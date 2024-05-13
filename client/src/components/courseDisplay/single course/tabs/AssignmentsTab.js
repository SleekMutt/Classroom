import { Col, Container, Row, Card, Button, Stack, CardGroup } from 'react-bootstrap'
import { axiosAPI } from '../../../../api/axiosClient';
import { useNavigate } from 'react-router';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import PaginationComponent from '../../../pagination/PaginationComponent';
import AddAssignmentModal from './modals/AddAssignmentModal';
import { GrTask } from "react-icons/gr";

const AssignmentsTab = ({courseId }) => {
    const [inter, setInter] = useState(0) 
    const [showAddModal, setShowAddModal] = useState(false);
    const [assignmentsList, setAssignments] = useState([])
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const navigate = useNavigate()

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
      };

    const handleAddModal = () => {
        setCurrentPage(1)
        setInter(inter + 1)
    }

    useEffect(() => {
        const fetchAssignments = async () => {
          axiosAPI.get('/assignment/page-by-course-id',
            {
              headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
              },
              params: {
                courseId: courseId,
                page: currentPage - 1
              }
            }
          )
            .then(response => {
              setTotalPages(response.data.totalPages)
              setAssignments(response.data.content)
            })
            .catch(error => {
              console.log(error.response)
              navigate('/error', {
                state: {
                  code: error.message,
                  message: error.response.data.messages
                }
              })
            })
        };
    
        fetchAssignments();
      }, [currentPage, inter]);

    return (<Container className="p-4" style={{ maxWidth: '95%', margin: '0 auto' }}>
        <Row >
            <Col >
            </Col>
            <Col xs={4}  >    <div >
                <div style={{ width: '95%', margin: '0 auto' }}><h1 className='heading'>Assignments</h1></div>
                <div style={{ width: '100%', height: '2px', backgroundColor: 'black', margin: '10px auto' }}></div>
            </div></Col>
            <Col></Col>
        </Row>
        <Row>
            <Col></Col>
            <Col ><div style={{ width: '75%', margin: '0 auto' }}>
                {assignmentsList.map((elem, idx) => (
                    <Col key={idx}  >
                        <Card style={{ marginBottom: '20px', fontSize: '1rem' }}>
                            <Card.Header><GrTask style={{marginRight: '10px'}}></GrTask>{elem.name ? elem.name : "No name"}</Card.Header>
                            <Card.Body>
                                <Card.Subtitle style={{  fontSize: '1rem' }}>
                                    {elem.createdAt ? new Date(elem.createdAt).toLocaleDateString('en', { year: 'numeric', month: 'short', day: '2-digit', }) : "No creation date was found"}
                                </Card.Subtitle>
                                <Card.Text>
                                    {elem.description ? elem.description : "No description available"}
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </div></Col>
            <Col >
            {window.location.href.includes('owned-courses') ? <Button className="btn-block btn-sm" variant="success" onClick={() => setShowAddModal(true)}>Add assignment</Button> : <></>}
            </Col>
        </Row>
        <Row >
            <Col></Col>
            <Col><PaginationComponent setCurrentPage={handlePageChange} currentPage={currentPage} totalPages={totalPages} /></Col>
            <Col></Col>
        </Row>
        {showAddModal && <AddAssignmentModal handleClose={() => setShowAddModal(false)} courseId={courseId} handleAdd={handleAddModal}></AddAssignmentModal>}

    </Container>
    )
};

export default AssignmentsTab;

