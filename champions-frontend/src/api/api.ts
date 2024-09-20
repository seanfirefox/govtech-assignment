import axios, { AxiosInstance } from 'axios';
import { Team, Match } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const addTeams = async (teamsInput: string): Promise<Team[]> => {
  try {
    const response = await axiosInstance.post('/teams/batch', teamsInput, {
      headers: {
        'Content-Type': 'text/plain',
      },
    });
    return response.data as Team[];
  } catch (error: any) {
    throw error.response?.data || 'Failed to add teams.';
  }
};

export const addMatches = async (matchesInput: string): Promise<Match[]> => {
  try {
    const response = await axiosInstance.post('/matches/batch', matchesInput, {
      headers: {
        'Content-Type': 'text/plain',
      },
    });
    return response.data as Match[];
  } catch (error: any) {
    throw error.response?.data || 'Failed to add matches.';
  }
};

export const fetchMatches = async (): Promise<Match[]> => {
  try {
    const response = await axiosInstance.get(`/matches`);
    return response.data as Match[];
  } catch (error: any) {
    throw error.response?.data || 'Failed to fetch rankings.';
  }
};

export const fetchRankings = async (groupNumber: number): Promise<Team[]> => {
  try {
    const response = await axiosInstance.get(`/teams/rankings/${groupNumber}`);
    return response.data as Team[];
  } catch (error: any) {
    throw error.response?.data || 'Failed to fetch rankings.';
  }
};

export const fetchTeamDetails = async (teamId: number): Promise<Team> => {
  try {
    const response = await axiosInstance.get(`/teams/${teamId}`);
    return response.data as Team;
  } catch (error: any) {
    throw error.response?.data || 'Failed to fetch team details.';
  }
};

export const fetchTeams = async (): Promise<Team[]> => {
  try {
    const response = await axiosInstance.get(`/teams/`);
    return response.data as Team[];
  } catch (error: any) {
    throw error.response?.data || 'Failed to fetch rankings.';
  }
};


export const editTeam = async (teamId: number, updatedTeam: Team): Promise<Team> => {
  try {
    const response = await axiosInstance.put(`/teams/${teamId}`, updatedTeam);
    return response.data as Team;
  } catch (error: any) {
    throw error.response?.data || 'Failed to edit team.';
  }
};

export const deleteTeamById = async (teamId: number): Promise<void> => {
  try {
    await axiosInstance.delete(`/teams/${teamId}`);
  } catch (error: any) {
    throw error.response?.data || 'Failed to delete team.';
  }
};

export const deleteMatchById = async (matchId: number): Promise<void> => {
  try {
    await axiosInstance.delete(`/matches/${matchId}`);
  } catch (error: any) {
    throw error.response?.data || 'Failed to delete match.';
  }
};

export const clearAllTeams = async (): Promise<void> => {
  try {
    await axiosInstance.delete('/teams/clear');
  } catch (error: any) {
    throw error.response?.data || 'Failed to clear teams.';
  }
};

export const clearAllMatches = async (): Promise<void> => {
  try {
    await axiosInstance.delete('/matches/clear');
  } catch (error: any) {
    throw error.response?.data || 'Failed to clear matches.';
  }
};
