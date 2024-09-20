import React from 'react';
import { Button } from 'react-bootstrap';
import { FaTrash } from 'react-icons/fa';

interface DeleteButtonProps {
  onClick: () => void;
  variant?: string;
  title?: string;
}

const DeleteButton: React.FC<DeleteButtonProps> = ({ onClick, variant = 'danger', title = 'Delete' }) => {
  return (
    <Button variant={variant} size="sm" onClick={onClick} title={title}>
      <FaTrash />
    </Button>
  );
};

export default DeleteButton;