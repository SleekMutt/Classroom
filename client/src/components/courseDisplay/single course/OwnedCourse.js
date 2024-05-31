import { axiosAPI } from '../../../api/axiosClient';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { useParams } from 'react-router';
import { Tabs, Tab } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import EditTab from './tabs/EditTab';
import StudentsTab from './tabs/Students';
import AssignmentsTab from './tabs/AssignmentsTab';

const OwnedCourse = () => {
  const navigate = useNavigate();
  let { id } = useParams();
  const [key, setKey] = useState('assignments');
  const [course, setCourse] = useState('');
  const [assignmentsList, setAssignments] = useState('');

  useEffect(() => {
    axiosAPI.get(`/course/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    })
      .then(response => {
        const {  assignments, ...restCourse } = response.data;
        console.log(response.data)
        setCourse(restCourse)
        setAssignments(assignments)
      })
      .catch(error => {
        console.log(error.response)
        navigate('/error', {
          state: {
            code: error.message,
            message: error.response.data.messages
          }
        })
      })

  }, []);

  const onTabSelect = (eventKey, event) => {
    setKey(eventKey)
  }

  return (
    <div>
      <div style={{ backgroundColor: 'white' }}>
        <div style={{ height: '25px', maxWidth: '95%', margin: '0 auto' }}>
          <span><Link to="/owned-courses">Courses</Link> / {course.name}</span>
        </div>
      </div>
      <div className='tabs' >
        <div style={{ maxWidth: '1000px', margin: '0 auto', paddingTop: '10px', wordWrap: 'break-word' }}>
          <h4 className="text-left">{course.name}</h4>
          <h6 className="text-left mb-4 gray-text">{course.description}</h6>
        </div>
        <div style={{ maxWidth: '600px', margin: '0 auto' }}>
          <Tabs activeKey={key} justify onSelect={onTabSelect} style={{ textColor: 'black', borderBottom: 'none' }}>
            <Tab eventKey="assignments" title="Assignments" >
            </Tab>
            <Tab eventKey="students" title="Students">
            </Tab>
            <Tab eventKey="edit" title="Edit">
            </Tab>
          </Tabs>
        </div>
      </div>
      {(() => {
        if(course){
          switch (key) {
            case 'assignments':
              return <AssignmentsTab assignments={assignmentsList} courseId={course.id}/>;
            case 'students':
              return <StudentsTab joiningCode={course.joiningCode} students={course.students} courseId={course.id} />;
            case 'edit':
              return <EditTab course={course} setCourse={setCourse}/>;
            default:
              return <div>Default content</div>;
          }
        }
      })()}
    </div>
  );

};

export default OwnedCourse;