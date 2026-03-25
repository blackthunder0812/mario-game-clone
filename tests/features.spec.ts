import { test, expect } from '@playwright/test';

test.describe('Features Section', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('displays features section heading', async ({ page }) => {
    const heading = page.locator('#features h2');
    await expect(heading).toHaveText('Features');
  });

  test('renders all six feature cards', async ({ page }) => {
    const features = page.locator('.feature');
    await expect(features).toHaveCount(6);
  });

  test.describe('feature cards content', () => {
    const expectedFeatures = [
      { attr: 'platformer', title: 'Side-Scrolling' },
      { attr: 'enemies', title: 'Enemies' },
      { attr: 'coins', title: 'Coins' },
      { attr: 'physics', title: 'Physics' },
      { attr: 'levels', title: 'Level Design' },
      { attr: 'hud', title: 'HUD & Score' },
    ];

    for (const { attr, title } of expectedFeatures) {
      test(`shows "${title}" feature card`, async ({ page }) => {
        const card = page.locator(`.feature[data-feature="${attr}"]`);
        await expect(card).toBeVisible();
        await expect(card.locator('h3')).toHaveText(title);
        // Each card has a description paragraph
        await expect(card.locator('p')).not.toBeEmpty();
      });
    }
  });

  test('each feature card has an icon', async ({ page }) => {
    const icons = page.locator('.feature .icon');
    await expect(icons).toHaveCount(6);
    for (let i = 0; i < 6; i++) {
      await expect(icons.nth(i)).not.toBeEmpty();
    }
  });
});
