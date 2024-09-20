import React, { useEffect, useState } from 'react';
import { fetchMatches } from '../api/api';
import { Match } from '../types';
import { Table, Container, Alert, Spinner } from 'react-bootstrap';

const MatchHistory: React.FC = () => {
  const [matches, setMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');

  const fetchMatchHistory = async () => {
    try {
      const data = await fetchMatches();
      setMatches(data);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching match history:', err);
      setError('Failed to load match history.');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMatchHistory();
  }, []);

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
      <h2>Match History</h2>
      <Table striped bordered hover responsive className="mt-3">
        <thead>
          <tr>
            <th>#</th>
            <th>Team A</th>
            <th>Goals A</th>
            <th>Team B</th>
            <th>Goals B</th>
            <th>Result</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {matches.map((match, index) => (
            <tr key={match.id}>
              <td>{index + 1}</td>
              <td>{match.teamAName}</td>
              <td>{match.goalsA}</td>
              <td>{match.teamBName}</td>
              <td>{match.goalsB}</td>
              <td>{getResult(match)}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

const getResult = (match: Match): string => {
  if (match.goalsA > match.goalsB) {
    return `${match.teamAName} Wins`;
  } else if (match.goalsA < match.goalsB) {
    return `${match.teamBName} Wins`;
  } else {
    return 'Draw';
  }
};

export default MatchHistory;
