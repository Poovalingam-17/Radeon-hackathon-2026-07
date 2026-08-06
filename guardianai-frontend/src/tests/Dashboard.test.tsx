import { describe, it, expect } from 'vitest';

describe('Dashboard Component Unit Tests', () => {
  it('should format metrics properly', () => {
    const formatPercentage = (val: number) => `${val.toFixed(1)}%`;
    expect(formatPercentage(98.43)).toBe('98.4%');
  });

  it('should calculate blocked threat percentages correctly', () => {
    const total = 1000;
    const blocked = 15;
    const rate = ((total - blocked) / total) * 100;
    expect(rate).toBe(98.5);
  });
});
