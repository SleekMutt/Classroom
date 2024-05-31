import { Card, Form, Button, FloatingLabel } from "react-bootstrap";
import { useState } from "react";
import { axiosAPI } from "../../../../api/axiosClient";
import { useNavigate } from "react-router";
const EditTab = ({course, setCourse}) => {
    const [formData, setFormData] = useState({
        name: course.name,
        description: course.description
      });
    const navigate = useNavigate()
      const handleChange = (e) => {
        console.log(course)
        const { name, value } = e.target;
        setFormData(prevState => ({
          ...prevState,
          [name]: value
        }));
      };
    
      const handleSubmit = (e) => {
        e.preventDefault();
        console.log(formData);
        axiosAPI.put('/course/', {
            id: course.id,
            name: formData.name,
            description: formData.description
          }, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          })
            .then(response => {
              setCourse(response.data)
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
    <Card style={{width: '30%', margin: '0 auto', marginTop: '20px'}}>
      <Card.Body>
        <Card.Title>Edit course</Card.Title>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="formName" style={{marginBottom: '10px'}}>
            <FloatingLabel label="Name">
                <Form.Control type="text" name="name" value={formData.name} onChange={handleChange} placeholder="Enter your name" />
            </FloatingLabel>
          </Form.Group>
          <Form.Group controlId="formEmail">
            <FloatingLabel label="Description">
                <Form.Control type=" " name="description" value={formData.description} onChange={handleChange} placeholder="Enter description" />
            </FloatingLabel>
          </Form.Group>
          <Button variant="primary" type="submit" style={{marginTop: '20px'}}>
            Submit
          </Button>
        </Form>
      </Card.Body>
    </Card>
    
  )

};

export default EditTab;