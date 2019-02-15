INSERT [dbo].[StatusAgv] ([cor], [descricao]) VALUES (N'#ccc', N'RODANDO')
INSERT [dbo].[StatusAgv] ([cor], [descricao]) VALUES (N'#ddd', N'PARADO')

INSERT [dbo].[Agv] ([id], [descricao], [mac], [tipoAgv]) VALUES (CAST(1 AS Numeric(19, 0)), N'AGV 1', N'AAAABBBBCCCCDDDD', N'REBOCADOR')
INSERT [dbo].[Agv] ([id], [descricao], [mac], [tipoAgv]) VALUES (CAST(2 AS Numeric(19, 0)), N'AGV 2', N'DDDDCCCCBBBBAAAA', N'CARREGADOR')
