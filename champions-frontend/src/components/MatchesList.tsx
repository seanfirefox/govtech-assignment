import React, { useEffect, useState } from 'react';
import { fetchMatches, deleteMatchById } from '../api/api';
import { Match } from '../types';
import { Table, Container, Spinner, Alert, Button, Modal } from 'react-bootstrap';
import DeleteButton from './DeleteButton';

const MatchesList: React.FC = () => {
  const [matches, setMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [showModal, setShowModal] = useState<boolean>(false);
  const [matchToDelete, setMatchToDelete] = useState<Match | null>(null);

  useEffect(() => {
    const getMatches = async () => {
      try {
        const data = await fetchMatches();
        setMatches(data);
      } catch (err: any) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    getMatches();
  }, []);

  const handleDelete = async (matchId: number) => {
    try {
      await deleteMatchById(matchId);
      setMatches(matches.filter((match) => match.id !== matchId));
    } catch (err: any) {
      setError(err);
    }
  };

  const confirmDelete = (match: Match) => {
    setMatchToDelete(match);
    setShowModal(true);
  };

  const handleConfirmDelete = () => {
    if (matchToDelete) {
      handleDelete(matchToDelete.id);
      setShowModal(false);
      setMatchToDelete(null);
    }
  };

  const handleCancelDelete = () => {
    setShowModal(false);
    setMatchToDelete(null);
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
      <h2>Matches List</h2>
      <Table striped bordered hover responsive className="mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Team A</th>
            <th>Team B</th>
            <th>Goals A</th>
            <th>Goals B</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {matches.map((match) => (
            <tr key={match.id}>
              <td>{match.id}</td>
              <td>{match.teamAName}</td>
              <td>{match.teamBName}</td>
              <td>{match.goalsA}</td>
              <td>{match.goalsB}</td>
              <td>
                {/* Add other action buttons like Edit if necessary */}
                <DeleteButton onClick={() => confirmDelete(match)} title="Delete Match" />
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
          {matchToDelete ? (
            <p>
              Are you sure you want to delete the match between <strong>{matchToDelete.teamAName}</strong> and{' '}
              <strong>{matchToDelete.teamBName}</strong>?
            </p>
          ) : (
            <p>Are you sure you want to delete this match?</p>
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

export default MatchesList;
