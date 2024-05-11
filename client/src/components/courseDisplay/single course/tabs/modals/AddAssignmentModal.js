import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { axiosAPI } from "../../../../../api/axiosClient";

const AddAssignmentModal = ({ handleClose, handleAdd, courseId}) => {
  const [form, setForm] = useState({ name: "" , description: ""});
  const navigate = useNavigate();

  const joinCourse = (event) => {
    event.preventDefault();
    axiosAPI.post('/assignment/', {
        ...form,
        course: {
            id: courseId
        }
    }, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
      .then(response => {
        handleAdd(response.data)
      })
      .catch(error => {
        navigate('/error', {
          state: {
            code: error.message,
            message: error.response.data.messages
          }
        })
      }).finally(() => {
        handleClose()
      });;
  }

  const formChange = (event) => {
    const { name, value } = event.target;
    setForm(prevForm => ({ ...prevForm, [name]: value }));
  };

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Form onSubmit={joinCourse}>
        <Modal.Header closeButton>
          <Modal.Title>Join course</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Assignment name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Test task №1"
              autoFocus
              name='name'
              value={form.name}
              onChange={formChange}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Assignment description</Form.Label>
            <Form.Control
              type="text"
              placeholder="Test task description"
              autoFocus
              name='description'
              value={form.description}
              onChange={formChange}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" type="submit" >
            Join Course
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default AddAssignmentModal;