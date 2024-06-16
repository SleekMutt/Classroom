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
  const [errors, setErrors] = useState({});

  const joinCourse = (event) => {
    event.preventDefault();
    if(validateForm()){
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
    } else if (form.description.length < 15) {
      formIsValid = false;
      errors.description = 'Description should be at least 15 characters long';
    }


    if (form.deadline === "") {
      formIsValid = false;
      errors.deadline = 'Deadline is required';
    } else if (new Date(form.deadline) <=  new Date()) {
      formIsValid = false;
      errors.deadline = 'Deadline must be in the future';
    }
    if (files.length === 0) {
      formIsValid = false;
      errors.files = 'Files are required';
    } 

    if (form.rating === "") {
      formIsValid = false;
      errors.rating = 'Rating is required';
    } else if (form.rating <= 0) {
      formIsValid = false;
      errors.rating = 'Rating must be positive';
    }

    setErrors(errors);
    return formIsValid;
  };

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
            {errors.name && <Form.Text className="text-danger">{errors.name}</Form.Text>}
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
          {errors.description && <Form.Text className="text-danger">{errors.description}</Form.Text>}
          <Form.Group className="mb-3" style={{ display: 'flex', flexDirection: "column" }}>
            <Form.Label>Deadline date</Form.Label>
            <ReactDatePicker
              showIcon
              dateFormat="MMMM d, yyyy h:mm aa"

              showTimeInput
              selected={form.deadline}
              onChange={(date) => setForm(prevForm => ({ ...prevForm, "deadline": date }))}></ReactDatePicker>
              {errors.deadline && <Form.Text className="text-danger">{errors.deadline}</Form.Text>}
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
                  File {indx + 1}: {file.path}
                </li>
              ))}</ul>
            </aside>
          </section>
          {errors.files && <Form.Text className="text-danger">{errors.files}</Form.Text>}

          <Form.Group className="mb-3">
            <Form.Label>Rating</Form.Label>
            <Form.Control
              type="number"
              placeholder="Maximum rating of the assignment"
              autoFocus
              name='rating'
              value={form.rating}
              onChange={formChange}
            />
          {errors.rating && <Form.Text className="text-danger">{errors.rating}</Form.Text>}
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