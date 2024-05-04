import { axiosAPI } from '../../api/axiosClient';
import { useEffect,useState } from 'react';
import { useNavigate } from 'react-router';
import CommonCourseDisplay from './CommonCourseDisplay';

const JoinedCourses = () => {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axiosAPI.get('/course/joined-courses', {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
    .then(response => {
      setCourses(response.data);
    })
    .catch(error => {
      console.log(error.response)
      navigate('/error', {state: {
          code: error.message,
          message: error.response.data.messages
      }})          
  })

  }, []);


  return (<><CommonCourseDisplay title="Joined courses" courses={courses}/>
  </>);

};

export default JoinedCourses;