/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ContentpageComponentsPage, ContentpageDeleteDialog, ContentpageUpdatePage } from './contentpage.page-object';

const expect = chai.expect;

describe('Contentpage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contentpageUpdatePage: ContentpageUpdatePage;
  let contentpageComponentsPage: ContentpageComponentsPage;
  let contentpageDeleteDialog: ContentpageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Contentpages', async () => {
    await navBarPage.goToEntity('contentpage');
    contentpageComponentsPage = new ContentpageComponentsPage();
    await browser.wait(ec.visibilityOf(contentpageComponentsPage.title), 5000);
    expect(await contentpageComponentsPage.getTitle()).to.eq('jackApp.contentpage.home.title');
  });

  it('should load create Contentpage page', async () => {
    await contentpageComponentsPage.clickOnCreateButton();
    contentpageUpdatePage = new ContentpageUpdatePage();
    expect(await contentpageUpdatePage.getPageTitle()).to.eq('jackApp.contentpage.home.createOrEditLabel');
    await contentpageUpdatePage.cancel();
  });

  it('should create and save Contentpages', async () => {
    const nbButtonsBeforeCreate = await contentpageComponentsPage.countDeleteButtons();

    await contentpageComponentsPage.clickOnCreateButton();
    await promise.all([contentpageUpdatePage.setTitleInput('title'), contentpageUpdatePage.setContentInput('content')]);
    expect(await contentpageUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await contentpageUpdatePage.getContentInput()).to.eq('content', 'Expected Content value to be equals to content');
    await contentpageUpdatePage.save();
    expect(await contentpageUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contentpageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Contentpage', async () => {
    const nbButtonsBeforeDelete = await contentpageComponentsPage.countDeleteButtons();
    await contentpageComponentsPage.clickOnLastDeleteButton();

    contentpageDeleteDialog = new ContentpageDeleteDialog();
    expect(await contentpageDeleteDialog.getDialogTitle()).to.eq('jackApp.contentpage.delete.question');
    await contentpageDeleteDialog.clickOnConfirmButton();

    expect(await contentpageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
