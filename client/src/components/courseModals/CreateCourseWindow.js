import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { axiosAPI } from '../../api/axiosClient'
import { useNavigate } from "react-router-dom";
import { createBrowserHistory } from "history";

const CreateCourseWindow = ({ handleClose }) => {
  const [form, setForm] = useState({ name: "", description: "" });
  const navigate = useNavigate();
  const history = createBrowserHistory();
  const [errors, setErrors] = useState({});

  const createCourse = (event) => {
    event.preventDefault();
    if(validateForm()){
      axiosAPI.post('/course/', {
        name: form.name,
        description: form.description
      }, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
        }
      })
        .then(response => {
          history.push('/owned-courses/' + response.data.id)
          window.location.reload()
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
        });
    }

  }

  const validateForm = () => {
    let errors = {};
    let formIsValid = true;

    if (form.name === "" ) {
      formIsValid = false;
      errors.name = 'Name is required';
    } else if (form.name.length < 6) {
      formIsValid = false;
      errors.name = 'Name should be at least 6 characters long';
    }

    if (form.description === "") {
      formIsValid = false;
      errors.description = 'Description is required';
    } else if (form.description.length < 6) {
      formIsValid = false;
      errors.description = 'Description should be at least 15 characters long';
    }

    setErrors(errors);
    console.log(errors)
    return formIsValid;
  };

  const formChange = (event) => {
    const { name, value } = event.target;
    setForm(prevForm => ({ ...prevForm, [name]: value }));
  };

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Form onSubmit={createCourse}>
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
            {errors.name && <Form.Text className="text-danger">{errors.name}</Form.Text>}
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Description</Form.Label>
            <Form.Control
              type="text"
              placeholder="This course is about..."
              autoFocus
              name='description'
              value={form.description}
              onChange={formChange}
            />
          </Form.Group>
          {errors.description && <Form.Text className="text-danger">{errors.description}</Form.Text>} 
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