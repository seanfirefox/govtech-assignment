import React, { useEffect, useState } from 'react';
import { fetchTeamDetails, fetchMatches } from '../api/api';
import { Team, Match } from '../types';
import { Container, Spinner, Alert, Table } from 'react-bootstrap';
import { useParams } from 'react-router-dom';

const TeamDetails: React.FC = () => {
  const { teamId } = useParams<{ teamId?: string }>();
  const [team, setTeam] = useState<Team | null>(null);
  const [matches, setMatches] = useState<Match[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const getTeamDetails = async () => {
      if (!teamId) {
        setError('No team ID specified.');
        setLoading(false);
        return;
      }

      try {
        const id = parseInt(teamId, 10);
        if (isNaN(id)) {
          setError('Invalid team ID.');
          setLoading(false);
          return;
        }

        const teamData = await fetchTeamDetails(id);
        setTeam(teamData);

        const allMatches = await fetchMatches();
        const teamMatches = allMatches.filter(
          (match) => match.teamAName === teamData.name || match.teamBName === teamData.name
        );
        setMatches(teamMatches);
      } catch (err: any) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    getTeamDetails();
  }, [teamId]);

  if (loading) {
    return (
      <Container className="mt-4 text-center">
        <Spinner animation="border" variant="primary" />
      </Container>
    );
  }

  if (error || !team) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error || 'Team not found.'}</Alert>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <h2>Team Details: {team.name}</h2>
      <p><strong>Registration Date:</strong> {team.registrationDate}</p>
      <p><strong>Group Number:</strong> {team.groupNumber}</p>
      <p><strong>Points:</strong> {team.totalMatchPoints}</p>

      <h3 className="mt-4">Matches Played:</h3>
      {matches.length === 0 ? (
        <p>No matches played yet.</p>
      ) : (
        <Table striped bordered hover responsive className="mt-3">
          <thead>
            <tr>
              <th>Match ID</th>
              <th>Opponent</th>
              <th>Goals Scored</th>
              <th>Goals Conceded</th>
              <th>Outcome</th>
            </tr>
          </thead>
          <tbody>
            {matches.map((match) => {
              const isTeamA = match.teamAName === team.name;
              const opponent = isTeamA ? match.teamBName : match.teamAName;
              const goalsScored = isTeamA ? match.goalsA : match.goalsB;
              const goalsConceded = isTeamA ? match.goalsB : match.goalsA;
              let outcome = 'Draw';
              if (goalsScored > goalsConceded) {
                outcome = 'Win';
              } else if (goalsScored < goalsConceded) {
                outcome = 'Loss';
              }

              return (
                <tr key={match.id}>
                  <td>{match.id}</td>
                  <td>{opponent}</td>
                  <td>{goalsScored}</td>
                  <td>{goalsConceded}</td>
                  <td>{outcome}</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default TeamDetails;