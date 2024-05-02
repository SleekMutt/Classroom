import React from 'react';
import '../styles/components/error.css'
import ErrorIcon from '../assets/error-icon.png';
import { useLocation } from 'react-router';

const ErrorComponent = (props) => {
  const location = useLocation();
  const { code, message } = location.state || { code: props.code, message: props.message };
  return (
    <div style={styles.container}>
      <img src={ErrorIcon} alt="Error" style={styles.image} />
      <div style={styles.text}>
        <h1 style={styles.heading}>{code}</h1>
        <p style={styles.message}>{message}</p>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'column',
    height: '100vh',
    position: 'relative',
  },
  image: {
    width: '20%',
    maxWidth: '300px',
    marginBottom: '20px',
  },
  text: {
    textAlign: 'center',
  },
  heading: {
    fontSize: '3vw',
    marginBottom: '10px',
    color: '#333',
  },
  message: {
    fontSize: '1.5vw',
    color: '#666',
  },
};

export default ErrorComponent;