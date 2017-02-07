package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Ross on 06/02/2017.
 */
public class UserTest {

    private Board board;
    private User userDavid;
    private User userRoss;
    private Question questionOnDeepLearning;
    private Answer answerOnDeepLearning;
    private String exceptionMessage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        board = new Board("Artificial-Intelligence");
        userDavid = new User(board, "David");
        userRoss = new User(board, "Ross");
        questionOnDeepLearning = new Question(userRoss, "What is deep-learning");
        answerOnDeepLearning = new Answer(questionOnDeepLearning, userDavid, "Deep learning is a branch of machine learning");
        board.addQuestion(questionOnDeepLearning);
        board.addAnswer(answerOnDeepLearning);
        exceptionMessage = String.format("Only %s can accept this answer as it is their question",
                answerOnDeepLearning.getQuestion().getAuthor().getName());

    }

    @Test
    public void upvotingUserQuestionIncreasesReputationByFivePoints () {
        userDavid.upVote(questionOnDeepLearning);

        assertEquals(5, userRoss.getReputation());
    }


    @Test
    public void upvotingUserAnswerIncreasesReputationByTenPoints() {
        userRoss.upVote(answerOnDeepLearning);

        assertEquals(10, userDavid.getReputation());
    }

    @Test
    public void havingAnswerAcceptedIncreasesReputationByFifteenPoints(){
        userRoss.acceptAnswer(answerOnDeepLearning);

        assertEquals(15, userDavid.getReputation());
    }

    @Test
    public void votingOnUsersOwnQuestionsOrAnswersThrowsException () throws Exception {
        thrown.expect(VotingException.class);
        userRoss.upVote(questionOnDeepLearning);
    }

    @Test
    public void votingOnUsersOwnAnswersThrowsException() throws Exception {
        thrown.expect(VotingException.class);
        userRoss.downVote(questionOnDeepLearning);
    }

    @Test
    public void originalQuestionAuthorIsAbleToAcceptAnswer() {
        userRoss.acceptAnswer(answerOnDeepLearning);

        assertEquals(true, answerOnDeepLearning.isAccepted());
    }

    @Test
    public void userAcceptingAnswerToQuestionForWhichTheyAreNotTheAuthor () throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(exceptionMessage);

        userDavid.acceptAnswer(answerOnDeepLearning);

        assertEquals(false, answerOnDeepLearning.isAccepted());
    }

    @Test
    public void downVotingUserAnswerSubtractsReputationByNumberOfDownVotes () {
        userRoss.downVote(answerOnDeepLearning);

        assertEquals(-1, userDavid.getReputation());
    }

    @Test
    public void upVotingUserAnswerAndAnswerAcceptedIncreasesReputationByTwentyFivePoints() {
        userRoss.upVote(answerOnDeepLearning);
        userRoss.acceptAnswer(answerOnDeepLearning);

        assertEquals(25, userDavid.getReputation());
    }
}

