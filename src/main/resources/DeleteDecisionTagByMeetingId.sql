DELETE FROM Decision_Tag
WHERE DecisionId IN (
    SELECT Id
    FROM Decision
    WHERE LinkedMeeting = ?
    )