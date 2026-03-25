import { test, expect } from '@playwright/test';

test.describe('Landing Page', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('has correct page title', async ({ page }) => {
    await expect(page).toHaveTitle('Super Mario Clone');
  });

  test('displays header with game name and description', async ({ page }) => {
    const heading = page.locator('header h1');
    await expect(heading).toHaveText('Super Mario Clone');

    const description = page.locator('header p');
    await expect(description).toContainText('pure Java');
  });

  test('renders all navigation links', async ({ page }) => {
    const nav = page.locator('nav');
    await expect(nav).toBeVisible();

    const links = nav.locator('a');
    await expect(links).toHaveCount(4);
    await expect(links.nth(0)).toHaveText('Features');
    await expect(links.nth(1)).toHaveText('Controls');
    await expect(links.nth(2)).toHaveText('Game Stats');
    await expect(links.nth(3)).toHaveText('How to Run');
  });

  test('nav links point to correct sections', async ({ page }) => {
    await expect(page.locator('nav a[href="#features"]')).toBeVisible();
    await expect(page.locator('nav a[href="#controls"]')).toBeVisible();
    await expect(page.locator('nav a[href="#stats"]')).toBeVisible();
    await expect(page.locator('nav a[href="#run"]')).toBeVisible();
  });
});
