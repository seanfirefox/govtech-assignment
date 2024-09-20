import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import AddTeams from './components/AddTeams';
import AddMatches from './components/AddMatches';
import Rankings from './components/Rankings';
import TeamDetails from './components/TeamDetails';
import EditTeam from './components/EditTeam';
import TeamsList from './components/TeamsList';
import MatchesList from './components/MatchesList';
import ClearData from './components/ClearData';
import 'bootstrap/dist/css/bootstrap.min.css';

const App: React.FC = () => {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/add-team" element={<AddTeams />} />
        <Route path="/teams" element={<TeamsList />} />
        <Route path="/add-matches" element={<AddMatches />} />
        <Route path="/matches" element={<MatchesList />} />
        <Route path="/rankings" element={<Rankings />} />
        <Route path="/rankings/:groupNumber" element={<Rankings />} />
        <Route path="/team/:teamId/details" element={<TeamDetails />} />
        <Route path="/team/:teamId/edit" element={<EditTeam />} />
        <Route path="/clear-data" element={<ClearData />} />
        {/* Add other routes as needed */}
      </Routes>
    </Router>
  );
};

export default App;