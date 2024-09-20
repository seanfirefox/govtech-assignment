import React, { useEffect, useState } from 'react';
import { fetchTeams, deleteTeamById } from '../api/api';
import { Team } from '../types';
import { Table, Container, Spinner, Alert, Button, Modal } from 'react-bootstrap';
import DeleteButton from './DeleteButton';

const TeamsList: React.FC = () => {
  const [teams, setTeams] = useState<Team[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [showModal, setShowModal] = useState<boolean>(false);
  const [teamToDelete, setTeamToDelete] = useState<Team | null>(null);

  useEffect(() => {
    const getTeams = async () => {
      try {
        const data = await fetchTeams();
        setTeams(data);
      } catch (err: any) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    getTeams();
  }, []);

  const handleDelete = async (teamId: number) => {
    try {
      await deleteTeamById(teamId);
      setTeams(teams.filter((team) => team.id !== teamId));
    } catch (err: any) {
      setError(err);
    }
  };

  const confirmDelete = (team: Team) => {
    setTeamToDelete(team);
    setShowModal(true);
  };

  const handleConfirmDelete = () => {
    if (teamToDelete) {
      handleDelete(teamToDelete.id);
      setShowModal(false);
      setTeamToDelete(null);
    }
  };

  const handleCancelDelete = () => {
    setShowModal(false);
    setTeamToDelete(null);
  };

  if (loading) {
    return (
      <Container className="mt-4 text-center">
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <h2>Teams List</h2>
      <Table striped bordered hover responsive className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Registration Date</th>
            <th>Group Number</th>
            <th>Points</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {teams.map((team) => (
            <tr key={team.id}>
              <td>{team.id}</td>
              <td>{team.name}</td>
              <td>{team.registrationDate}</td>
              <td>{team.groupNumber}</td>
              <td>{team.totalMatchPoints}</td>
              <td>
                {/* Add other action buttons like Edit if necessary */}
                <DeleteButton onClick={() => confirmDelete(team)} title="Delete Team" />
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Delete Confirmation Modal */}
      <Modal show={showModal} onHide={handleCancelDelete}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {teamToDelete ? (
            <p>
              Are you sure you want to delete the team <strong>{teamToDelete.name}</strong>?
            </p>
          ) : (
            <p>Are you sure you want to delete this team?</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCancelDelete}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleConfirmDelete}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default TeamsList;