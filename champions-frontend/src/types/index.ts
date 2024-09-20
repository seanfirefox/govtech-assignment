export interface Team {
  id: number;
  name: string;
  registrationDate: string;
  groupNumber: number;
  totalMatchPoints: number;
}

export interface Match {
  id: number;
  teamAName: string;
  teamBName: string;
  goalsA: number;
  goalsB: number;
}