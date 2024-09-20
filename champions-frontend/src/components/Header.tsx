import React from 'react';
import { Navbar, Nav, Container, NavDropdown } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useNavigate } from 'react-router-dom';

const Header: React.FC = () => {
  const navigate = useNavigate();

  const handleSelectGroup = (groupNumber: number) => {
    navigate(`/rankings/${groupNumber}`);
  };

  const groups = [1, 2];

  return (
    <Navbar bg="light" expand="lg">
      <Container>
        <LinkContainer to="/">
          <Navbar.Brand>Team Manager</Navbar.Brand>
        </LinkContainer>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <LinkContainer to="/add-team">
              <Nav.Link>Add Team</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/teams">
              <Nav.Link>Teams List</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/add-matches">
              <Nav.Link>Add Matches</Nav.Link>
            </LinkContainer>
            <LinkContainer to="/matches">
              <Nav.Link>Matches List</Nav.Link>
            </LinkContainer>
            <NavDropdown title="Rankings" id="rankings-dropdown">
              <NavDropdown.Item onClick={() => navigate('/rankings')}>
                All Groups
              </NavDropdown.Item>
              <NavDropdown.Divider />
              {groups.map((group) => (
                <NavDropdown.Item key={group} onClick={() => handleSelectGroup(group)}>
                  Group {group}
                </NavDropdown.Item>
              ))}
            </NavDropdown>
            <LinkContainer to="/clear-data">
              <Nav.Link>Clear All Data</Nav.Link>
            </LinkContainer>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;