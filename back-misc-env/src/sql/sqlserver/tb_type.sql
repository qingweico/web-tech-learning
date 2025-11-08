-- ----------------------------
-- Table structure for tb_type
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[tb_type]') AND type IN ('U'))
	DROP TABLE [dbo].[tb_type]
GO

CREATE TABLE [dbo].[tb_type] (
  [id] nvarchar(19) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [type] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [create_time] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[tb_type] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Primary Key structure for table tb_type
-- ----------------------------
ALTER TABLE [dbo].[tb_type] ADD CONSTRAINT [PK__tb_type__3213E83F2B7287E2] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO

