import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { axiosAPI } from "../../../../../api/axiosClient";
import ReactDatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {useDropzone} from 'react-dropzone'
import { useCallback } from "react";

const AddAssignmentModal = ({ handleClose, handleAdd, courseId }) => {
  const [form, setForm] = useState({ name: "", description: "", deadline: "", rating: "" });
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
  
  const onDrop = useCallback(acceptedFiles => {
    const formData = new FormData();
    formData.append('file', acceptedFiles[0]);

    axiosAPI.post('/assignment/test', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });

  }
  )

  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})


  const formChange = (event) => {
    const { name, value } = event.target;
    setForm(prevForm => ({ ...prevForm, [name]: value }));
  };

  return (
    <Modal show={true} onHide={handleClose} centered>
      <Form onSubmit={joinCourse}>
        <Modal.Header closeButton>
          <Modal.Title>Create assignment</Modal.Title>
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
          <Form.Group className="mb-3" style={{ display: 'flex', flexDirection: "column" }}>
            <Form.Label>Deadline date</Form.Label>
            <ReactDatePicker
              showIcon
              dateFormat="MMMM d, yyyy h:mm aa"

              showTimeInput
              selected={form.deadline}
              onChange={(date) => setForm(prevForm => ({ ...prevForm, "deadline": date }))}></ReactDatePicker>
          </Form.Group>
          <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
              isDragActive ?
                <p>Drop the files here ...</p> :
                <p>Drag 'n' drop some files here, or click to select files</p>
            }
          </div>

          <Form.Group className="mb-3">
            <Form.Label>Rating</Form.Label>
            <Form.Control
              type="number"
              placeholder="Maximum rating of the assignment"
              autoFocus
              name='rating'
              min='1'
              value={form.rating}
              onChange={formChange}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" type="submit" >
            Create
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default AddAssignmentModal;