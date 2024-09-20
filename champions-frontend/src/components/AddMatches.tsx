import React, { useState } from 'react';
import { addMatches } from '../api/api';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';

const AddMatches: React.FC = () => {
  const [matchesText, setMatchesText] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const handleAddMatches = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccessMessage('');
    setErrorMessage('');
    setLoading(true);

    try {
      await addMatches(matchesText);
      setSuccessMessage('Matches added successfully!');
      setMatchesText('');
    } catch (error: any) {
      setErrorMessage(error);
    }

    setLoading(false);
  };

  const handleClear = () => {
    setMatchesText('');
    setSuccessMessage('');
    setErrorMessage('');
  };

  return (
    <Container className="mt-4">
      <h2>Add Match Results</h2>
      <Form onSubmit={handleAddMatches}>
        <Form.Group controlId="matchesInput">
          <Form.Label>Enter Matches (one per line)</Form.Label>
          <Form.Control
            as="textarea"
            rows={10}
            placeholder={`firstTeam secondTeam 0 3\nthirdTeam fourthTeam 1 1`}
            value={matchesText}
            onChange={(e) => setMatchesText(e.target.value)}
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" className="mt-3" disabled={loading}>
          {loading ? <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> : 'Add Matches'}
        </Button>
        <Button variant="secondary" type="button" className="mt-3 ms-2" onClick={handleClear} disabled={loading}>
          Clear
        </Button>
      </Form>

      {successMessage && (
        <Alert variant="success" className="mt-4">
          {successMessage}
        </Alert>
      )}

      {errorMessage && (
        <Alert variant="danger" className="mt-4">
          {errorMessage}
        </Alert>
      )}
    </Container>
  );
};

export default AddMatches;