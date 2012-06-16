Chess Master 9001
=====
A Java application which allows users to design variants on chess, specifying new kinds of pieces and how they move, the board layout, the initial positions of pieces, how the game is won, and special rules.
Users can play either as two humans at one computer, two humans at different computers through a network connection, or one human versus the computer.
The AI for the "computer" player is be defined by plug-ins that users can write.

---

How to Contribute
-----------------

_Of course, there may be alternate ways to do many of these steps, but these are the ones we will help you with if you get stuck_

### Getting the Code
* First things first: Install and set up [SmartGit](http://www.syntevo.com/smartgit/index.html)
 * Choose non-commercial use when the installer asks
 * The installer should automatically detect the location of your git executable _(Of course, this means if you don't have git installed, you need to go do that first)_
 * Tell the installer that your hosting provider is github and log in with your credentials (if this doesn't work for you, just select "no host provider" and input the URL of your fork when it asks you)
 * Also, go ahead and use SmartGit as SSH (unless you have a compelling reason not to)
* Next, come back to github and Fork your own copy of the [chess repository](https://github.com/drewhannay/chess)
* Back in SmartGit, choose Clone and put in the URL of your forked copy of chess
 * Choose where to save your local code, then give the project a name in SmartGit and hit Finish
 * Next we need to set up a link to the original repository so we can keep our fork up to date with the latest code.
 * Click the "Remote" menu item in SmartGit, then select "Manage Remotes" from the list and click "Add".
 * Enter <code>upstream</code> as the name (no caps!) and https://github.com/drewhannay/chess as the URL, then press "Add".
 * Now close the Remote Manager and open the Branch Manager by clicking the "Branch" menu item, and then "Branch Manager". You should see an "upstream/master" remote branch with no local branch. Click on that item and click add, giving it the name "upstream-master", then click "Add Branch".
 * You can now close the Branch Manager and celebrate, knowing that you have finished setting up your repository!
 
### Running the Code
* Download and set up [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-classic-372/indigosr2)
* In Eclipse, click New Java Project, then give the project a name and uncheck the "Use Default Location" checkbox and choose the location of your local copy of the code, then click finish.
* Right-click on the project folder in Eclipse and click Import
 * Choose General -> Preferences as the Import Source
 * Choose chessProject.epf from your newly downloaded local code as the input preference file, and make sure "Import All" is checked, then click Finish. This will make sure your Eclipse code clean up tools follow the code guidelines for our project.
 * If you have errors in the project at this point, make sure your Eclipse is set to use the 1.6 Java Compiler (Preferences->Java->Compiler)
* You should now be able to run the project just by choosing "Run" in Eclipse!
 * Note that every time you switch branches in SmartGit (See [Making and Committing Changes](https://github.com/drewhannay/chess#making-and-committing-your-changes) below), you will need to refresh your project in Eclipse before making any changes to the code, since switching branches actually changes the contents of some of the project files.

### The Issue System
This project uses the github Issues system to track all work being done on the app.  To get started, visit the [Issues Tab](https://github.com/drewhannay/chess/issues) and browse the list of things that need to be done.  Find one that looks interesting and isn't yet being worked on and claim it for yourself by leaving a comment saying you're working on it.  If you find a bug in the app or have a feature or enhancement idea, add an issue for it. <b>Every single commit</b> that gets added to the project should have an associated issue number (as specified below...)

### Making and Committing Your Changes
Once you've picked an issue to work on and claimed it as your own in the comments, it's time to start coding! Well, almost...here's where things get a little complicated (*but I promise, this is worthwhile!*).  We <b>highly</b> recommend you make a separate branch for the changes you'll be working on. But you say "I don't even know what a branch is! Why would I want to make one?"  Well, my friend, if this sounds like you, you should take some time and read this [tutorial on Git](http://git-scm.com/book/en/Git-Basics).  Back? Okay. Here's the steps you need to follow:

* Go to SmartGit and make sure you have the project we made earlier with your fork open.
* At the top of the screen, click the "Branch" menu item and then click "Add Branch" and give your new branch a name (*something like "MyChanges"...it doesn't really matter what you call it*), then click "Add Branch & Switch"
* You are now ready to code!

When you are ready to commit, go back to SmartGit. Your changed files should appear in the the main section of the window. Select each of the files you want to commit and press the "Stage" button located directly above the list of files.  Next, click "Commit" (next to the Stage button).  You will now be required to enter a commit messgae. Each commit should be associated with an Issue number from our [Issues list](https://github.com/drewhannay/chess/issues). Your commit messages should be in this form (where "N" is the appropriate issue number):

<code>Short sentence describing what was changed. Issue #N<br /><br />Any other description required should go in paragraph form below the initial message with a new line separating the two</code>

This formatting is very important because with it, github will conveniently link your commit message to the related issue number.

### Updating Your Fork
Frequently you will want to update your forked copy of the repository with changes from the main repo. There's a few steps required for this:

* Switch to your <code>upstream-master</code> branch and press the large "Pull" button and select the remote branch to pull from (https://github.com/drewhannay/chess).
* After that is finished, switch to your <code>origin/master</code> branch and press "Merge". You should see a log of all the commit messages along with labels indicating the last commit for each branch. Click on the latest commit (which should have the label <code>upstream-master</code>) and click "Merge".
* Next, commit those files as you would any other changes, but use the commit message that SmartGit provides for you, then click "Push" to push the changes to your remote fork.
* You can repeat this process to merge changes into any of your other branches; just switch to the branch you want to update and hit "Merge".

### Getting Your Changes Merged with the Main Repository
So you made some changes and added a cool new feature or fixed a pesky bug. You probably want to know how to get those changes included in the main repository. Well, it's your lucky day...

* Go to your forked repository on github and click "Branches" and find the branch you want merged with the main repo.
* Once you're looking at that branch, click "Pull Request" to open a message to the repo admins with your commit message in the subject line. Here you can add a message to the admins describing your changes or clarifying anything you think they might find confusing when they're reviewing your code.
* Once you're done with your message, just hit "Pull Request" to send the request on it's way.
* Now one of the admins will review your changes at their earliest convenience and if they think there are any changes you need to make, they will leave a comment on the appropriate line, sending you a notification.
 * If they leave any comments, you'll get a notification on github and you can review their comments.
 * After you've made those changes on your local branch, commit them and push again, except this time your commit message should say <code>Code review. Issue #N</code>, where N is your issue number. This commit is automatically added to your pull request, so you don't need to make another one.
 * Repeat these steps until your code is approved!
* Once the admins accept your code, you will be able to see a new commit on the main repo with your changes and all contributors will be able to pull them and update.

### Now what?
For more information on the project, visit our [Project Wiki](https://github.com/drewhannay/chess/wiki) or contact one of the administrators or other contributors and we'll be more than happy to help you get started!