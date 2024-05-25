import { Modal, Button, Form } from "react-bootstrap";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { axiosAPI } from "../../../../api/axiosClient";
import { Spinner } from "react-bootstrap";
const EditCommentComponent = ({ id, repositoryName, handleClose, comment, handleUpdate }) => {
    const [form, setForm] = useState({ body: comment });
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const updateComment = (event) => {
        event.preventDefault();
        setLoading(true)
        axiosAPI.put('/assignment/reviews', {
            ...form,
            id,
            repositoryName: `ClassroomSleek/${repositoryName}`
        }, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
            .then(response => {
                handleUpdate(response.data)
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

    const formChange = (event) => {
        const { name, value } = event.target;
        setForm(prevForm => ({ ...prevForm, [name]: value }));
    };

    return (
        <>
            <Modal show={true} onHide={handleClose} centered>
                <Form onSubmit={updateComment}>
                    <Modal.Header closeButton>
                        <Modal.Title>Update comment</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {!loading ? <Form.Group className="mb-3">
                            <Form.Label>Comments body</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Comment to update"
                                autoFocus
                                name='body'
                                value={form.body}
                                onChange={formChange}
                            />
                        </Form.Group> :
                            (<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                <Spinner animation="border" role="status">
                                    <span className="visually-hidden">Loading...</span>
                                </Spinner>
                            </div>)}
                    </Modal.Body>
                    {!loading ? <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>
                            Close
                        </Button>
                        <Button variant="warning" type="submit">
                            Update comment
                        </Button>
                    </Modal.Footer> : <></>}
                </Form>
            </Modal>
        </>
    );
}

export default EditCommentComponent;

