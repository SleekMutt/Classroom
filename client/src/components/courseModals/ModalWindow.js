import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { axiosAPI } from '../../api/axiosClient'
import { useNavigate } from "react-router-dom";
import { createBrowserHistory } from "history";

const JoinCourseWindow = ({ handleClose }) => {
  const [form, setForm] = useState({ code: "" });
  const navigate = useNavigate();
  const history = createBrowserHistory()
  const joinCourse = (event) => {
    event.preventDefault();

    axiosAPI.patch('/course/join-course', null, {
      params: {
        code: form.code
      },
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
      .then(response => {
        history.push('/joined-courses/' + response.data.id)
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
            <Form.Label>Course code</Form.Label>
            <Form.Control
              type="text"
              placeholder="89a52c973f8640e89cf360980780b6fc"
              autoFocus
              name='code'
              value={form.code}
              onChange={formChange}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" type="submit">
            Join Course
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default JoinCourseWindow;