import { test, expect } from '@playwright/test';

test.describe('Navigation & Scroll', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('clicking Features nav link scrolls to features section', async ({ page }) => {
    await page.click('nav a[href="#features"]');
    await expect(page.locator('#features')).toBeInViewport();
  });

  test('clicking Controls nav link scrolls to controls section', async ({ page }) => {
    await page.click('nav a[href="#controls"]');
    await expect(page.locator('#controls')).toBeInViewport();
  });

  test('clicking Game Stats nav link scrolls to stats section', async ({ page }) => {
    await page.click('nav a[href="#stats"]');
    await expect(page.locator('#stats')).toBeInViewport();
  });

  test('clicking How to Run nav link scrolls to run section', async ({ page }) => {
    await page.click('nav a[href="#run"]');
    await expect(page.locator('#run')).toBeInViewport();
  });
});

test.describe('Accessibility', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('page has a lang attribute', async ({ page }) => {
    const lang = await page.locator('html').getAttribute('lang');
    expect(lang).toBe('en');
  });

  test('nav has aria-label', async ({ page }) => {
    const label = await page.locator('nav').getAttribute('aria-label');
    expect(label).toBeTruthy();
  });

  test('all images/icons have text alternatives (feature icons have text siblings)', async ({ page }) => {
    const features = page.locator('.feature');
    const count = await features.count();
    for (let i = 0; i < count; i++) {
      const feature = features.nth(i);
      // Each feature card has an h3 that serves as the text label
      await expect(feature.locator('h3')).not.toBeEmpty();
    }
  });

  test('page uses semantic HTML structure', async ({ page }) => {
    await expect(page.locator('header')).toHaveCount(1);
    await expect(page.locator('main')).toHaveCount(1);
    await expect(page.locator('footer')).toHaveCount(1);
    await expect(page.locator('nav')).toHaveCount(1);
  });
});

test.describe('Responsive Layout', () => {
  test('features grid adjusts on mobile viewport', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/');

    const features = page.locator('#features');
    await expect(features).toBeVisible();

    // All feature cards should still be visible on mobile
    const cards = page.locator('.feature');
    const count = await cards.count();
    expect(count).toBe(6);
    for (let i = 0; i < count; i++) {
      await expect(cards.nth(i)).toBeVisible();
    }
  });

  test('page renders correctly on tablet viewport', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await page.goto('/');

    await expect(page.locator('header h1')).toBeVisible();
    await expect(page.locator('.controls-table')).toBeVisible();
    await expect(page.locator('.stats')).toBeVisible();
  });

  test('page renders correctly on desktop viewport', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/');

    await expect(page.locator('header h1')).toBeVisible();
    await expect(page.locator('#features')).toBeVisible();
    await expect(page.locator('#controls')).toBeVisible();
    await expect(page.locator('#stats')).toBeVisible();
    await expect(page.locator('#run')).toBeVisible();
  });
});
