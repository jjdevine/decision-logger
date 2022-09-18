SELECT d.*
FROM Decision d
         INNER JOIN Decision_DecisionMaker dm on dm.DecisionId = d.Id
         INNER JOIN Decision_Tag t on t.DecisionId = d.Id
WHERE dm.id IN (DM_PLACEHOLDERS)
   OR t.id IN (T_PLACEHOLDERS)