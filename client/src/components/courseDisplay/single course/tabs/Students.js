import { Col, Container, Row, Card, Button } from 'react-bootstrap'
import { axiosAPI } from '../../../../api/axiosClient';
import { useNavigate } from 'react-router';
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import PaginationComponent from '../../../pagination/PaginationComponent';

const StudentsTab = ({ joiningCode,  courseId }) => {
  const [ghStudents, setGhStudents] = useState([]);
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchGHUsers = async () => {
      axiosAPI.get('/user/gh-list',
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
          setGhStudents(response.data.content)
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

    fetchGHUsers();
  }, [currentPage]);

  const handleDelete = (index) => {
    axiosAPI.patch('/course/delete-user-from-course',
      null,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
          courseId: courseId,
          userId: index
        }
      }
    )
      .then(response => {
        setGhStudents(ghStudents.filter(obj => obj.id !== index))
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
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };
  return (
    <Container className="p-4" style={{ maxWidth: '95%', margin: '0 auto' }}>
      <Row >
        <Col >
          <Card style={{ width: '200px', border: '1px solid grey' }}>
            <Card.Body>
              <Card.Title>Joining code</Card.Title>
              <Card.Text className='text-center' style={{ fontSize: '14px' }}>
                {joiningCode}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col xs={4}  >    <div >
          <div style={{ width: '95%', margin: '0 auto' }}><h1 style={{ fontSize: '29px' }}>Students</h1></div>
          <div style={{ width: '100%', height: '2px', backgroundColor: 'black', margin: '10px auto' }}></div>
        </div></Col>
        <Col></Col>
      </Row>
      <Row>
        <Col></Col>
        <Col><div style={{ width: '75%', margin: '0 auto' }}>
          {ghStudents.map((elem, idx) => (
            <Col key={idx} >
              <Card style={{ marginBottom: '20px' }}>
                <Card.Body style={{ backgroundColor: 'transparent' }}>
                  <Card.Title>          <Link to={`https://github.com/${elem.login}`} className='courses-link'>
                    <img src={elem.avatarUrl} alt="User" style={{ marginRight: '5px', width: '25px', height: '25px', borderRadius: '50%' }} />{elem.login ? elem.login : "No name"}
                  </Link>
                  </Card.Title>
                </Card.Body>
                <Card.Footer style={{ backgroundColor: 'transparent', display: 'flex', justifyContent: 'flex-end' }}>
                  <Button variant="danger" size="sm" onClick={() => handleDelete(elem.id)}>
                    Delete
                  </Button>
                </Card.Footer>
              </Card>
            </Col>
          ))}
        </div></Col>
        <Col>

        </Col>
      </Row>
      <Row >
        <Col></Col>
        <Col><PaginationComponent setCurrentPage={handlePageChange} currentPage={currentPage} totalPages={totalPages} /></Col>
        <Col></Col>

      </Row>
    </Container>
  )

};

export default StudentsTab;