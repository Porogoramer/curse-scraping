/**
 * Downloads the html from a curseforge page after
 * waiting for the file-row-details elements to load
 * @param {String} URL to scrape
 * @param {String} filename name of the file to be saved
 */

import puppeteer from 'puppeteer';
import fs from 'fs';

if (process.argv.length != 4) {
  throw new Error(`URL and filename not provided got ${process.argv}`);
}

const url = process.argv[2];

async function downloadPage() {
  const browser = await puppeteer.launch({headless: false});
  const page = await browser.newPage();
  console.log('browser on')
  page.setDefaultNavigationTimeout(0);

  console.log('Navigating to page')
  await page.goto(url, { waitUntil: 'networkidle2' });
  
  // Wait for a specific element that indicates the page has fully loaded
  // Adjust the selector based on the actual page content
  await page.waitForSelector('.file-row-details', { timeout:600000 });

  // await new Promise(r => setTimeout(r, 1000));

  console.log('fetching content')
  const content = await page.content();

  console.log('saving output')
  fs.writeFileSync(process.argv[3], content);
  
  console.log('Closing browser')
  await browser.close();
}

downloadPage();
