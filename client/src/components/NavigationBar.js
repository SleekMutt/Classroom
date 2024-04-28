import React, {useEffect, useState} from 'react';
import {Nav, Navbar, NavDropdown, Container, Button} from "react-bootstrap";
import '../styles/components/navbar.css'
import BookIcon from '../assets/book-icon.svg';
import authService from '../services/AuthService';
import { useNavigate } from 'react-router';
import CreateCourseWindow from './courseModals/CreateCourseWindow';
import JoinCourseWindow from './courseModals/ModalWindow';

const NavigationBar = () => {
  const [loggedIn, setLoggedIn] = useState(authService.isAuthenticated());
  const [showJoinModal, setShowJoinModal] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {
    const authSubscription = authService.isAuthenticated$().subscribe(isAuthenticated => {
      setLoggedIn(isAuthenticated);
    });

    return () => {
      authSubscription.unsubscribe();
    };
  }, []);
  const navigate = useNavigate();


  return (
    <div>
      <Navbar collapseOnSelect expand="lg" className="bg-body-tertiary custom-navbar">
        <Container>
          <Navbar.Brand href="/">  
            <img src={BookIcon} alt="Book Icon" />       
            {' '}
            Bohdan's classroom
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link  onClick={() => loggedIn ? setShowJoinModal(true) : navigate('/login')}>Join course</Nav.Link>
              <Nav.Link onClick={() => loggedIn ? setShowCreateModal(true) : navigate('/login')}>Create course</Nav.Link>
              <NavDropdown title="Courses" id="collapsible-nav-dropdown">
                <NavDropdown.Item href="owned-courses">Owned courses</NavDropdown.Item>
                <NavDropdown.Item href="joined-courses">Joined courses</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Nav>
              <Button href="login" className='login-button'>{loggedIn ? 'Log out' : "Login"}</Button>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      {showJoinModal && <JoinCourseWindow handleClose={() => setShowJoinModal(false)} />}
      {showCreateModal && <CreateCourseWindow handleClose={() => setShowCreateModal(false)} />}
    </div>
  );
};

export default NavigationBar;
