
import React, { useState } from 'react';
import { clearAllTeams, clearAllMatches } from '../api/api';
import { Button, Container, Alert, Spinner, Modal } from 'react-bootstrap';

const ClearData: React.FC = () => {
  const [showModal, setShowModal] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');

  const handleClearData = async () => {
    setLoading(true);
    setSuccessMessage('');
    setErrorMessage('');

    try {
      await clearAllMatches();
      await clearAllTeams();
      setSuccessMessage('All data cleared successfully.');
    } catch (error: any) {
      setErrorMessage(error);
    }

    setLoading(false);
    setShowModal(false);
  };

  return (
    <Container className="mt-4">
      <h2>Clear All Data</h2>
      <Button variant="danger" onClick={() => setShowModal(true)}>
        Clear All Teams and Matches
      </Button>

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

      {/* Confirmation Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Clear Data</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you want to delete all teams and matches? This action cannot be undone.
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleClearData} disabled={loading}>
            {loading ? <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> : 'Clear All'}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default ClearData;