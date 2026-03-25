import { test, expect } from '@playwright/test';

test.describe('How to Run Section', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('displays how to run heading', async ({ page }) => {
    await expect(page.locator('#run h2')).toHaveText('How to Run');
  });

  test('mentions JDK 17 requirement', async ({ page }) => {
    const section = page.locator('#run');
    await expect(section).toContainText('JDK 17');
  });

  test('shows run instructions code block', async ({ page }) => {
    const instructions = page.locator('.run-instructions');
    await expect(instructions).toBeVisible();
    await expect(instructions).toContainText('./run.sh');
    await expect(instructions).toContainText('javac');
    await expect(instructions).toContainText('java -cp out mario.Main');
  });
});

test.describe('Footer', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('displays footer with credit', async ({ page }) => {
    const footer = page.locator('footer');
    await expect(footer).toBeVisible();
    await expect(footer).toContainText('pure Java');
  });

  test('contains GitHub link', async ({ page }) => {
    const link = page.locator('footer a');
    await expect(link).toHaveText('Source on GitHub');
    await expect(link).toHaveAttribute('href', /github\.com/);
  });
});
