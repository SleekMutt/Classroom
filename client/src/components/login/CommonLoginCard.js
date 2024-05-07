import { Card, Button } from "react-bootstrap";

const CommonLoginCard = ({ title, onClick, text, buttonText }) => {
  return (
    <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '50vh' }}>
      <Card className="text-center" style={{ width: '25rem', border: 'none', borderRadius: '20px' }}>
        <Card.Body>
          <Card.Title>{title}</Card.Title>
          <Card.Text>{text}</Card.Text>
          <Button variant="primary" onClick={onClick}>{buttonText}</Button>
        </Card.Body>
      </Card>
    </div>
  );
};

export default CommonLoginCard;