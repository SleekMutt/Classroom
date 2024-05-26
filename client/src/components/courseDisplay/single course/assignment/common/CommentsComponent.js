import { useEffect, useState } from "react";
import { Card,  Spinner, Button, Col, InputGroup, Form } from "react-bootstrap";
import { useNavigate } from "react-router";
import { axiosAPI } from "../../../../../api/axiosClient";
import { Link } from "react-router-dom";
import EditCommentComponent from "./EditCommentComponent";
const CommentsComponent = ({repositoryName, user}) => {
    const [loadingComment, setLoadingComment] = useState(true)
    const [comments, setComments] = useState([])
    const [form, setForm] = useState({ comment: "" });
    const navigate = useNavigate();
    const [selectedComment, setSelectedComment] = useState(null);
    
    const handleClose = () => {
        setSelectedComment(null)
    }
    const handleUpdate = (updatedComment) => {
        setComments(comments.map(elem => {return elem.id === updatedComment.id ? updatedComment : elem}))
    }

    const formChange = (event) => {
        const { name, value } = event.target;
        setForm(prevForm => ({ ...prevForm, [name]: value }));
      };
    const addComment = (event) => {
        event.preventDefault();
        setLoadingComment(false)
        axiosAPI.post(`/assignment/reviews`, {
          body: form.comment,
          repositoryName: `ClassroomSleek/${repositoryName}`
        },
          {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          }
        )
          .then(response => {
            setComments([...comments, response.data])
          })
          .catch(error => {
    
            navigate('/error', {
              state: {
                code: error.message,
                message: error.response.data.messages
              }
            })
          })
          .finally(() => {
            setForm({ comment: "" })
            setLoadingComment(true)
          })
      }
      useEffect(() => {
        axiosAPI.get('/assignment/reviews', {
            params: {
              repositoryName: `ClassroomSleek/${repositoryName}`
            },
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          }).then(response => {
            setComments(response.data)
          }).catch(error => {
            navigate('/error', {
              state: {
                code: error.message,
                message: error.response.data.messages
              }
            })
          })
      },
    [])

    const deleteComment = (id) => {
        axiosAPI.delete('/assignment/reviews', {
            params: {
                repositoryName: `ClassroomSleek/${repositoryName}`,
                id
              },
              headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
              }
        }).then(response => {
            setComments(comments.filter(elem => elem.id !== id))
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
      
    return (<>
        {
            loadingComment ?
                (<Form onSubmit={addComment} style={{ marginTop: '40px' }} >
                    <Form.Group className="mb-3">
                        <InputGroup className="mb-3">
                            <Form.Control
                                as="textarea"
                                rows={4}
                                type="text"
                                placeholder="Write your comment"
                                autoFocus
                                name='comment'
                                value={form.comment}
                                onChange={formChange}
                                style={{ resize: 'none' }}
                            />

                        </InputGroup>
                        <div className="d-flex justify-content-end">
                            <Button variant="primary" type="submit">
                                Comment
                            </Button>
                        </div>
                    </Form.Group>
                </Form>)
                :
                (<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', paddingTop: '80px' }}>
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                </div>)
            
        }
                 {comments.map((elem, idx) => (
                <Col key={idx} >
                  <Card style={{ width: "80%", margin: '20px auto' }}>
                    <Card.Body style={{ backgroundColor: 'transparent' }}>
                      <Card.Title>          <Link to={`https://github.com/${elem.user.login}`} className='courses-link'>
                        <img src={elem.user.avatarUrl} alt="User" style={{ marginRight: '5px', width: '25px', height: '25px', borderRadius: '50%' }} />{elem.user.login ? elem.user.login : "No name"}
                      </Link>
                      </Card.Title>
                      <Card.Subtitle>
                        {new Date(elem.createdAt).toLocaleString('en', {
                          year: 'numeric',
                          month: 'short',
                          day: '2-digit',
                          hour: '2-digit',
                          minute: '2-digit',
                          hour12: false
                        })}
                      </Card.Subtitle>
                      <Card.Body>
                        {elem.body}
                      </Card.Body>
                    </Card.Body>
                    {
                      user.gitHubUsername === elem.user.login ? <Card.Footer>
                        <div  className="d-flex justify-content-end" style={{gap: '5px'}}>
                        <Button variant="warning" onClick={() => setSelectedComment(elem)}>
                            Edit
                        </Button>
                        <Button variant="danger" onClick={() => deleteComment(elem.id)}>
                            Delete
                        </Button>
                        </div>
                        </Card.Footer> : <></>
                    }
                  </Card>
                </Col>
              ))}
              {selectedComment ? <EditCommentComponent handleUpdate={handleUpdate} handleClose={handleClose} id={selectedComment.id} repositoryName={repositoryName} comment={selectedComment.body}></EditCommentComponent> : null}
    </>
    )
}

export default CommentsComponent;