import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import {axiosAPI} from '../../api/axiosClient'
import { useNavigate } from "react-router-dom";

const CreateCourseWindow = ({handleClose}) => { 
    const [form, setForm] = useState({name: ""});
    const navigate = useNavigate();

    const joinCourse = (event) => {
        event.preventDefault();

        axiosAPI.post('/course/', {
            name: form.name
        }, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          })
          .then(response => {
            console.log('Response:', response.data);
          })
          .catch(error => {
            navigate('/error', {state: {
                code: error.message,
                message: error.response.data.messages
            }})          
        }).finally( () => {
        handleClose()
        });
    }

    const formChange = (event) => {
        const { name, value } = event.target;
        setForm(prevForm => ({ ...prevForm, [name]: value }));
    };

    return (
        <Modal show={true} onHide={handleClose} centered>
                    <Form onSubmit={joinCourse}>
          <Modal.Header closeButton>
            <Modal.Title>Create Course</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Course name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Math course"
                autoFocus
                name='name'
                value={form.name}
                onChange={formChange}
              />
            </Form.Group>
                </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Close
            </Button>
            <Button variant="primary" type="submit">
              Create Course
            </Button>
          </Modal.Footer>
        </Form>  
        </Modal>
    );
  }
  
  export default CreateCourseWindow;