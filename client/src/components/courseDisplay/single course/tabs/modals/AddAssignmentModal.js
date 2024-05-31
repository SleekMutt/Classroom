import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { axiosAPI } from "../../../../../api/axiosClient";
import ReactDatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { useDropzone } from 'react-dropzone'
import { useCallback } from "react";
import { styled } from 'styled-components';

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

const AddAssignmentModal = ({ handleClose, handleAdd, courseId }) => {
  const [form, setForm] = useState({ name: "", description: "", deadline: "", rating: "" });
  const navigate = useNavigate();
  const [files, setFiles] = useState([])

  const joinCourse = (event) => {
    event.preventDefault();
    const formData = new FormData();

    let assignment = {
      ...form,
      course: {
        id: courseId
      }
    }
    formData.append(
      'assignment',
      new Blob([JSON.stringify(assignment)], {
        type: 'application/json'
      })
    );

    files.forEach(file => {
      formData.append('files', file);
    });

    axiosAPI.post('/assignment/', formData, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
        'Content-Type': 'multipart/form-data'
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
    setFiles(acceptedFiles)
  }
  )

  const { getRootProps, getInputProps, isDragActive, isFocused, isDragAccept, isDragReject } = useDropzone({ onDrop })


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
              as="textarea"
              rows={5}
              style={{resize: "none"}}
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
          <section className="container">
            <Container {...getRootProps({ isFocused, isDragAccept, isDragReject })}>
              <input {...getInputProps()} />
              <p>Drag and drop some files here, or click to select files</p>
            </Container>
            <aside>
              <h4>Files</h4>
              <ul>{files.map((file, indx) => (
                <li key={file.path}>
                  File {indx + 1}: {file.path.replace(/\.\w+$/, '')}
                </li>
              ))}</ul>
            </aside>
          </section>

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