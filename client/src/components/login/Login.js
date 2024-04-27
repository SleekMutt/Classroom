import React, { Component, useState } from 'react';
import  authService from '../../services/AuthService'
import CommonLoginCard from './CommonLoginCard';
import { useEffect } from 'react';

const Login = () => {
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
    <CommonLoginCard
      title={loggedIn ? "Do you wish to log out?" : "Login with GitHub"}
      text={loggedIn ? "Please use the button to log out" : "Please follow the link to login"}
      buttonText={loggedIn ? "Log out" : "Authenticate"}
      onClick={() => {loggedIn ? authService.toLogOut() : authService.navigateToGitHubOAuth();}}
    />
  );
};

export default Login;
