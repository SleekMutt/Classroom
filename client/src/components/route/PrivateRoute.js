import { Navigate } from "react-router-dom";
import authService from "../../services/AuthService";
import React from 'react';

const PrivateRoute = ({children }) => {
  return authService.isAuthenticated() ? children : <Navigate to="/login" />;
};
export default PrivateRoute;