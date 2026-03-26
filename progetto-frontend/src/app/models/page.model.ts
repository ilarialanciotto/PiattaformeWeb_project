export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // pagina corrente
  size: number;
}
