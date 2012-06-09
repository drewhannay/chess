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
 * Tell the installer that your hosting provider is github and log in with your credentials
 * Also, go ahead and use SmartGit as SSH (unless you have a compelling reason not to)
* Next, come back to github and Fork your own copy of the [chess repository](https://github.com/drewhannay/chess)
* Back in SmartGit, choose Clone and put in the URL of your forked copy of chess
 * Choose where to save your local code, then give the project a name in SmartGit and hit Finish

### Running the Code
* Download and set up [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-classic-372/indigosr2)
* In Eclipse, click New Java Project, then give the project a name and uncheck the "Use Default Location" checkbox and choose the location of your local copy of the code.
 * If you have errors in the project at this point, make sure your Eclipse is set to use the 1.6 Java Compiler (Preferences->Java->Compiler)
* You should now be able to run the project just by choosing "Run" in Eclipse!

### The Issue System
This project uses the github Issues system to track all work being done on the app.  To get started, visit the [Issues Tab](https://github.com/drewhannay/chess/issues) and browse the list of things that need to be done.  Find one that looks interesting and isn't yet being worked on and claim it for yourself by leaving a comment saying you're working on it.  If you find a bug in the app or have a feature or enhancement idea, add an issue for it. <b>Every single commit</b> that gets added to the project should have an associated issue number (as specified below...)

### Making and Committing Your Changes
Once you've picked an issue to work on and claimed it as your own in the comments, it's time to start coding! Well, almost...here's where things get a little complicated (*but I promise, this is worthwhile!*).  We <b>highly</b> recommend you make a separate branch for the changes you'll be working on. But you say "I don't even know what a branch is! Why would I want to make one?"  Well, my friend, if this sounds like you, you should take some time and read this [tutorial on Git](http://git-scm.com/book/en/Git-Basics).  Back? Okay. Here's the steps you need to follow:
* Go to SmartGit and make sure you have the project we made earlier with your fork open.
* At the top of the screen, click the "Branch" menu item and then click "Add Branch" and give your new branch a name (*something like "MyChanges"...it doesn't really matter what you call it*), then click "Add Branch & Switch"
* You are now ready to code!
Each commit should be associated with an Issue number from our [Issues list](https://github.com/drewhannay/chess/issues). Your commit messages should be in this form (where "N" is the appropriate issue number):

<code>Short sentence describing what was changed. Issue #N</code>

<code>Any other description required should go in paragraph form below the initial message with a new line separating the two</code>

### Updating Your Fork
* Coming soon!

### Getting Your Changes Merged with the Main Repository
* Coming soon!