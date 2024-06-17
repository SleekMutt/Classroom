import { useNavigate } from 'react-router';
import { useParams } from 'react-router';
import { useEffect, useState } from 'react';
import { axiosAPI } from '../../../../api/axiosClient';
import { Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { Spinner } from 'react-bootstrap';
import CommentsComponent from './common/CommentsComponent';
import { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { styled } from 'styled-components';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const getColor = (props) => {
  if (props.isDragAccept) {
    return '#00e676';
  }
  if (props.isDragReject) {
    return '#ff1744';
  }
  if (props.isFocused) {
    return '#2196f3';
  }
  return '#eeeeee';
}

const Container = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-width: 2px;
  border-radius: 2px;
  border-color: ${props => getColor(props)};
  border-style: dashed;
  background-color: #fafafa;
  color: #bdbdbd;
  outline: none;
  transition: border .24s ease-in-out;
`;


const StudentAssignmentComponent = () => {
  const navigate = useNavigate();
  let { id } = useParams();
  const [assignment, setAssignment] = useState('')
  const [acceptedAssignment, setAcceptedAssignment] = useState(null)
  const [inter, setInter] = useState(0)
  const [loading, setLoading] = useState(true)
  const [images, setImages] = useState([])
  const [user, setUser] = useState(null)

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);


    stompClient.connect({
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
    }, (frame) => {
        stompClient.subscribe('/user/topic/openai', (message) => {
          console.log(message)
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

    fetchAssignment();
  }, []);


  useEffect(() => {
    const fetchAssignment = async () => {
      axiosAPI.get(`/assignment/accepted-assignment/${id}`,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        }
      )
        .then(response => {
          setAcceptedAssignment(response.data)
          setLoading(true)

        })
        .catch(error => {
          if (error.response.status === 404) {
            console.log(error)
          }
          else {
            navigate('/error', {
              state: {
                code: error.message,
                message: error.response.data.messages
              }
            })
          }
        })
    };

    fetchAssignment();
  }, [inter]);

  const acceptAssignment = () => {
    setLoading(false)
    axiosAPI.put(`/assignment/accept-assignment/${id}`, null,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
      }
    )
      .then(response => {
        setInter(inter + 1)
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


  const onDrop = useCallback(acceptedFiles => {
    setImages(acceptedFiles)
  })

  const addFiles = () => {
    const formData = new FormData();
    acceptedFiles.forEach(file => {
      formData.append('files', file);
    });
    axiosAPI.post(`/assignment/add-files-to-repository`, formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
          repositoryName: `ClassroomSleek/${acceptedAssignment.repositoryName}`
        }
      }
    )
      .then(response => {

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
        setImages([])

      })
  }

  const { acceptedFiles, getRootProps, getInputProps, isFocused, isDragAccept, isDragReject } = useDropzone({ onDrop });
  const files = images.map((file, indx) => (
    <li key={file.path}>
      File {indx + 1}: {file.path.replace(/\.\w+$/, '')}
    </li>
  ));

  return (
    <div>
      {assignment && loading ? (
        <div>
          <div style={{ backgroundColor: 'white', borderBottom: '1px solid' }}>
            <div style={{ height: '25px', maxWidth: '95%', margin: '0 auto' }}>
              <span><Link to={assignment.course ? `/joined-courses/${assignment.course.id}` : '/joined-courses'}>{assignment.course ? assignment.course.name : 'Course'}</Link>  </span>
            </div>
          </div>
          <div style={{ width: '70%', margin: '0 auto', paddingTop: '20px', display: 'flex', gap: '0px' }} className="cont">
            <div style={{ flex: '1 1 auto', minWidth: '100px', padding: '15px' }} >
              <h2>{assignment.name}</h2>
              <h5>    Posted at: {assignment.createdAt ? new Date(assignment.createdAt).toLocaleDateString('en', { year: 'numeric', month: 'short', day: '2-digit', }) : "No creation date was found"}</h5>
              <h5>    Deadline at: {assignment.deadline ? new Date(assignment.deadline).toLocaleDateString('en', { year: 'numeric', month: 'short', day: '2-digit', }) : "No deadline date was found"}</h5>
              <div style={{ width: '100%', height: '2px', backgroundColor: 'black', margin: '10px auto' }}></div>
              <h6>{assignment.description}</h6>
              {
                acceptedAssignment ?
                  (<CommentsComponent repositoryName={acceptedAssignment.repositoryName} user={user}></CommentsComponent>
                  ) : null
              }
            </div>
            <div style={{ flex: '0 1 30%', display: 'flex', flexDirection: 'column', gap: '20px' }}>
              <Card className="text-center" style={{ minWidth: '250px', maxWidth: '100%' }}>
                <Card.Body>
                  <Card.Title>Assignment status</Card.Title>
                  <Card.Subtitle>{acceptedAssignment ? <a href={`https://github.com/ClassroomSleek/${acceptedAssignment.repositoryName}`}>{acceptedAssignment.repositoryName}</a> : `Assignment isn't accepted`}</Card.Subtitle>
                  <Card.Text>{acceptedAssignment && acceptedAssignment.rating ? `${acceptedAssignment.rating}/` : ''}{assignment.rating} points</Card.Text>
                </Card.Body>
                {!acceptedAssignment ? <Card.Footer>{new Date() < new Date(assignment.deadline) ? <Button variant='success' onClick={acceptAssignment}>Accept</Button> : 'Deadline has already passed'}</Card.Footer> : <></>}
              </Card>
              {acceptedAssignment ? <section className="container">
                <Container {...getRootProps({ isFocused, isDragAccept, isDragReject })}>
                  <input {...getInputProps()} />
                  <p>Drag and drop some files here, or click to select files to push them to repository</p>
                </Container>
                <aside>
                  <h4>Files</h4>
                  <ul>{files}</ul>
                  {images.length > 0 ? <Button variant='success' onClick={addFiles}>Add files</Button> : <></>}
                </aside>
              </section> : <></>}
            </div>

          </div>

        </div>) :
        (
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Loading...</span>
            </Spinner>
          </div>
        )
      }
    </div >
  );

};

export default StudentAssignmentComponent;