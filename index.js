import puppeteer from 'puppeteer';
import fs from 'fs';

const url = 'https://www.curseforge.com/minecraft/mc-mods/geckolib/files/all?page=1&pageSize=100';

async function downloadPage() {
  const browser = await puppeteer.launch({headless: false});
  const page = await browser.newPage();
  console.log('browser on')
  page.setDefaultNavigationTimeout(0);

  console.log('Navigating to page')
  await page.goto(url, { waitUntil: 'networkidle2' });
  
  // Wait for a specific element that indicates the page has fully loaded
  // Adjust the selector based on the actual page content
  await page.waitForSelector( () => {
    const fileRows = document.querySelectorAll('.file-row');
    if (fileRows.length == 0) return false;
    Array(fileRows).every((row) => {
      const aTag = row.querySelector('a');
      if (aTag)
        return aTag.href != null && aTag.href != "";
      return false;
    })
  }, { timeout: 60000 });

  // await new Promise(r => setTimeout(r, 1000));

  console.log('fetching content')
  const content = await page.content();

  console.log('saving output')
  fs.writeFileSync('output.html', content);
  
  console.log('Closing browser')
  await browser.close();
}

downloadPage();
