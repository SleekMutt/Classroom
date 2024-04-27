import React, {useEffect, useState} from 'react';
import {Nav, Navbar, NavDropdown, Container, Button} from "react-bootstrap";
import '../styles/components/navbar.css'
import BookIcon from '../assets/book-icon.svg';
import authService from '../services/AuthService';

const NavigationBar = () => {
  const [loggedIn, setLoggedIn] = useState(authService.isAuthenticated());

  useEffect(() => {
    const authSubscription = authService.isAuthenticated$().subscribe(isAuthenticated => {
      setLoggedIn(isAuthenticated);
    });

    return () => {
      authSubscription.unsubscribe();
    };
  }, []);
  

  return (
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
            <Nav.Link href="join-course">Join course</Nav.Link>
            <Nav.Link href="create-course">Create course</Nav.Link>
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
  );
};

export default NavigationBar;
