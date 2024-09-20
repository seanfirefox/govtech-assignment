import React, { useState, useEffect } from 'react';
import { fetchTeamDetails, editTeam } from '../api/api';
import { Team } from '../types';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import { useParams } from 'react-router-dom';

const EditTeam: React.FC = () => {
  const { teamId } = useParams<{ teamId?: string }>();
  const [team, setTeam] = useState<Team | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [updating, setUpdating] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');

  useEffect(() => {
    const getTeamDetails = async () => {
      if (!teamId) {
        setErrorMessage('No team ID specified.');
        setLoading(false);
        return;
      }

      try {
        const id = parseInt(teamId, 10);
        if (isNaN(id)) {
          setErrorMessage('Invalid team ID.');
          setLoading(false);
          return;
        }

        const teamData = await fetchTeamDetails(id);
        setTeam(teamData);
      } catch (err: any) {
        setErrorMessage(err);
      } finally {
        setLoading(false);
      }
    };

    getTeamDetails();
  }, [teamId]);

  const handleUpdateTeam = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!team) return;
    setUpdating(true);
    setSuccessMessage('');
    setErrorMessage('');

    try {
      const updatedTeam = await editTeam(team.id, team);
      setTeam(updatedTeam);
      setSuccessMessage('Team updated successfully.');
    } catch (err: any) {
      setErrorMessage(err);
    }

    setUpdating(false);
  };

  if (loading) {
    return (
      <Container className="mt-4 text-center">
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  if (errorMessage || !team) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{errorMessage || 'Team not found.'}</Alert>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <h2>Edit Team: {team.name}</h2>
      {successMessage && <Alert variant="success">{successMessage}</Alert>}
      {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
      <Form onSubmit={handleUpdateTeam}>
        <Form.Group controlId="teamName">
          <Form.Label>Team Name</Form.Label>
          <Form.Control
            type="text"
            value={team.name}
            onChange={(e) => setTeam({ ...team, name: e.target.value })}
            required
          />
        </Form.Group>

        <Form.Group controlId="registrationDate" className="mt-3">
          <Form.Label>Registration Date (DD/MM)</Form.Label>
          <Form.Control
            type="text"
            value={team.registrationDate}
            onChange={(e) => setTeam({ ...team, registrationDate: e.target.value })}
            placeholder="e.g., 17/05"
            required
          />
        </Form.Group>

        <Form.Group controlId="groupNumber" className="mt-3">
          <Form.Label>Group Number</Form.Label>
          <Form.Control
            type="number"
            value={team.groupNumber}
            onChange={(e) =>
              setTeam({ ...team, groupNumber: parseInt(e.target.value, 10) })
            }
            required
            min={1}
          />
        </Form.Group>

        <Form.Group controlId="points" className="mt-3">
          <Form.Label>Points</Form.Label>
          <Form.Control
            type="number"
            value={team.totalMatchPoints}
            onChange={(e) =>
              setTeam({ ...team, totalMatchPoints: parseInt(e.target.value, 10) })
            }
            required
            min={0}
          />
        </Form.Group>

        <Button variant="primary" type="submit" className="mt-4" disabled={updating}>
          {updating ? (
            <Spinner
              as="span"
              animation="border"
              size="sm"
              role="status"
              aria-hidden="true"
            />
          ) : (
            'Update Team'
          )}
        </Button>
      </Form>
    </Container>
  );
};

export default EditTeam;