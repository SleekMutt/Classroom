import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
import KancIcon from '../../assets/kanc.jpg'
import { Link } from 'react-router-dom';

const CommonCourseDisplay = ({ courses, title}) => {
  return (
    <div >
      <h2 style={{ textAlign: 'center', marginTop: '20px', position: 'relative' }}>
       {title}
      </h2>
    <Row xs={1} md={2} lg={3} xl={4} xxl={5} className="g-4 px-4 py-4" style={{ width: '100%' }}>
      {courses.map((elem, idx) => (
        <Col key={idx}>
         <Link to={`${elem.id}`} className='courses-link'> 
          <Card>
            <Card.Img variant="top" src={KancIcon} style={{ height: '100px' }} />
            <Card.Body>
              <Card.Title>{elem.name ? elem.name : "No name"}</Card.Title>
              <Card.Text>
                {elem.description ? elem.description : "No description available."}
              </Card.Text>
            </Card.Body>
          </Card>
          </Link>
        </Col>
      ))}
    </Row>
    </div>
  );
};

export default CommonCourseDisplay;