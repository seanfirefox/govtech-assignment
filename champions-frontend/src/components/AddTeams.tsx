import React, { useState } from 'react';
import { addTeams } from '../api/api';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';

const AddTeams: React.FC = () => {
  const [teamsText, setTeamsText] = useState<string>('');
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const handleAddTeams = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccessMessage('');
    setErrorMessage('');
    setLoading(true);

    try {
      await addTeams(teamsText);
      setSuccessMessage('Teams added successfully!');
      setTeamsText('');
    } catch (error: any) {
      setErrorMessage(error);
    }

    setLoading(false);
  };

  const handleClear = () => {
    setTeamsText('');
    setSuccessMessage('');
    setErrorMessage('');
  };

  return (
    <Container className="mt-4">
      <h2>Add New Teams</h2>
      <Form onSubmit={handleAddTeams}>
        <Form.Group controlId="teamsInput">
          <Form.Label>Enter Teams (one per line)</Form.Label>
          <Form.Control
            as="textarea"
            rows={10}
            placeholder={`firstTeam 17/05 2\nsecondTeam 07/02 2\nthirdTeam 24/04 1\nfourthTeam 24/01 1`}
            value={teamsText}
            onChange={(e) => setTeamsText(e.target.value)}
            required
          />
        </Form.Group>
        <Button variant="primary" type="submit" className="mt-3" disabled={loading}>
          {loading ? <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> : 'Add Teams'}
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

export default AddTeams;