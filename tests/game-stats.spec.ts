import { test, expect } from '@playwright/test';

test.describe('Game Stats Section', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('displays game stats heading', async ({ page }) => {
    await expect(page.locator('#stats h2')).toHaveText('Game Stats');
  });

  test('shows all stat cards', async ({ page }) => {
    const stats = page.locator('.stat');
    await expect(stats).toHaveCount(6);
  });

  test('starting lives value matches Constants.STARTING_LIVES (3)', async ({ page }) => {
    const value = page.locator('[data-stat="lives"]');
    await expect(value).toHaveText('3');
  });

  test('coin score matches Constants.COIN_SCORE (100)', async ({ page }) => {
    const value = page.locator('[data-stat="coin-score"]');
    await expect(value).toHaveText('100');
  });

  test('enemy score matches Constants.ENEMY_SCORE (200)', async ({ page }) => {
    const value = page.locator('[data-stat="enemy-score"]');
    await expect(value).toHaveText('200');
  });

  test('flag bonus matches Constants.FLAG_SCORE (1000)', async ({ page }) => {
    const value = page.locator('[data-stat="flag-score"]');
    await expect(value).toHaveText('1000');
  });

  test('FPS matches Constants.FPS (60)', async ({ page }) => {
    const value = page.locator('[data-stat="fps"]');
    await expect(value).toHaveText('60');
  });

  test('tile size matches Constants.TILE_SIZE (32)', async ({ page }) => {
    const value = page.locator('[data-stat="tile-size"]');
    await expect(value).toHaveText('32');
  });

  test('each stat has a label', async ({ page }) => {
    const labels = page.locator('.stat .label');
    await expect(labels).toHaveCount(6);
    for (let i = 0; i < 6; i++) {
      await expect(labels.nth(i)).not.toBeEmpty();
    }
  });
});
