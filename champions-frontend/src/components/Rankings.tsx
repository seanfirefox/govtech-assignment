import React, { useEffect, useState } from 'react';
import { fetchRankings } from '../api/api';
import { Team } from '../types';
import { Table, Container, Spinner, Alert } from 'react-bootstrap';
import { useParams } from 'react-router-dom';

const Rankings: React.FC = () => {
  const { groupNumber } = useParams<{ groupNumber?: string }>();
  const [rankings, setRankings] = useState<Team[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [qualifiers, setQualifiers] = useState<Team[]>([]);

  useEffect(() => {
    const getRankings = async () => {
      if (!groupNumber) {
        setError('No group number specified.');
        setLoading(false);
        return;
      }

      try {
        const groupNum = parseInt(groupNumber, 10);
        if (isNaN(groupNum) || groupNum < 1) {
          setError('Invalid group number.');
          setLoading(false);
          return;
        }

        const data = await fetchRankings(groupNum);
        setRankings(data);
        determineQualifiers(data);
      } catch (err: any) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    getRankings();
  }, [groupNumber]);

  const determineQualifiers = (teams: Team[]) => {
    const qualified = teams.slice(0, 4);
    setQualifiers(qualified);
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
      <h2>Rankings for Group {groupNumber}</h2>
      <Table striped bordered hover responsive className="mt-3">
        <thead>
          <tr>
            <th>Position</th>
            <th>Team</th>
            <th>Points</th>
            <th>Registration Date</th>
          </tr>
        </thead>
        <tbody>
          {rankings.map((team, index) => (
            <tr key={team.id}>
              <td>{index + 1}</td>
              <td>{team.name}</td>
              <td>{team.totalMatchPoints}</td>
              <td>{team.registrationDate}</td>
            </tr>
          ))}
        </tbody>
      </Table>

      <h3>Qualifiers for Next Round:</h3>
      <Table striped bordered hover responsive className="mt-2">
        <thead>
          <tr>
            <th>Position</th>
            <th>Team</th>
            <th>Points</th>
          </tr>
        </thead>
        <tbody>
          {qualifiers.map((team, index) => (
            <tr key={team.id}>
              <td>{index + 1}</td>
              <td>{team.name}</td>
              <td>{team.totalMatchPoints}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default Rankings;