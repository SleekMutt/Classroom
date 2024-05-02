import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import KancIcon from '../assets/kanc.jpg'
import { axiosAPI } from '../api/axiosClient';
import { useEffect,useState } from 'react';
import { useNavigate } from 'react-router';

const OwnedCourses = () => {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axiosAPI.get('/course/owned-courses', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
    .then(response => {
      setCourses(response.data);
    })
    .catch(error => {
      console.log(error.response)
      navigate('/error', {state: {
          code: error.message,
          message: error.response.data.messages
      }})          
  })

  }, []);

    return (
        <Row xs={1}  md={2} lg={3} xl={4} className="g-4 px-4 py-4" style={{width: '100%'}}>
        {courses.map((elem, idx) => (
          <Col key={idx}>
            <Card>
              <Card.Img variant="top" src={KancIcon} style={{height: '100px' }}/>
              <Card.Body>
                <Card.Title>{elem.name ? elem.name : "No name"}</Card.Title>
                <Card.Text>
                  {elem.description ? elem.description : "No description available."}
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
  );
};

export default OwnedCourses;