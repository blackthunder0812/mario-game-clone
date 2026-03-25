import { test, expect } from '@playwright/test';

test.describe('Controls Section', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('displays controls section heading', async ({ page }) => {
    await expect(page.locator('#controls h2')).toHaveText('Controls');
  });

  test('renders controls table with header row', async ({ page }) => {
    const table = page.locator('.controls-table');
    await expect(table).toBeVisible();

    const headers = table.locator('th');
    await expect(headers.nth(0)).toHaveText('Action');
    await expect(headers.nth(1)).toHaveText('Keys');
  });

  test('lists all four control actions', async ({ page }) => {
    const rows = page.locator('.controls-table tbody tr');
    await expect(rows).toHaveCount(4);
  });

  test('Move Left action shows correct keys', async ({ page }) => {
    const row = page.locator('.controls-table tbody tr').nth(0);
    await expect(row.locator('td').first()).toHaveText('Move Left');
    const kbds = row.locator('kbd');
    await expect(kbds.nth(0)).toHaveText('←');
    await expect(kbds.nth(1)).toHaveText('A');
  });

  test('Move Right action shows correct keys', async ({ page }) => {
    const row = page.locator('.controls-table tbody tr').nth(1);
    await expect(row.locator('td').first()).toHaveText('Move Right');
    const kbds = row.locator('kbd');
    await expect(kbds.nth(0)).toHaveText('→');
    await expect(kbds.nth(1)).toHaveText('D');
  });

  test('Jump action shows correct keys', async ({ page }) => {
    const row = page.locator('.controls-table tbody tr').nth(2);
    await expect(row.locator('td').first()).toHaveText('Jump');
    const kbds = row.locator('kbd');
    await expect(kbds.nth(0)).toHaveText('Space');
    await expect(kbds.nth(1)).toHaveText('W');
    await expect(kbds.nth(2)).toHaveText('↑');
  });

  test('Start/Confirm action shows Enter key', async ({ page }) => {
    const row = page.locator('.controls-table tbody tr').nth(3);
    await expect(row.locator('td').first()).toHaveText('Start / Confirm');
    await expect(row.locator('kbd')).toHaveText('Enter');
  });
});
