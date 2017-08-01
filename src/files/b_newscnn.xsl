<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<style>
					/*----- Tabs -----*/
					.tabs {
					    width:100%;
					    display:inline-block;
					}
					 
					    /*----- Tab Links -----*/
					    /* Clearfix */
					    .tab-links:after {
					        display:block;
					        clear:both;
					        content:'';
					    }
					 
					    .tab-links li {
					        margin:0px 5px;
					        float:left;
					        list-style:none;
					    }
					 
					        .tab-links a {
					            padding:9px 15px;
					            display:inline-block;
					            border-radius:3px 3px 0px 0px;
					            background:#7FB5DA;
					            font-size:16px;
					            font-weight:600;
					            color:#4c4c4c;
					            transition:all linear 0.15s;
					        }
					 
					        .tab-links a:hover {
					            background:#a7cce5;
					            text-decoration:none;
					        }
					 
					    li.active a, li.active a:hover {
					        background:#fff;
					        color:#4c4c4c;
					    }
					 
					    /*----- Content of Tabs -----*/
					    .tab-content {
					        padding:15px;
					        border-radius:3px;
					        box-shadow:-1px 1px 1px rgba(0,0,0,0.15);
					        background:#fff;
					    }
					 
					        .tab {
					            display:none;
					        }
					 
					        .tab.active {
					            display:block;
					        }
				</style>

				<script src="jquery-1.11.1.min.js"></script>
				<script>
					jQuery(document).ready(function() {
					    jQuery('.tabs .tab-links a').on('click', function(e)  {
					        var currentAttrValue = jQuery(this).attr('href');
					 
					        // Show/Hide Tabs
					        jQuery('.tabs ' + currentAttrValue).slideDown(400).siblings().slideUp(400);

					        // Change/remove current tab to active
					        jQuery(this).parent('li').addClass('active').siblings().removeClass('active');
					 
					        e.preventDefault();
					    });
					});
			    </script>
			    <script>
					jQuery(document).ready(function() {
					    jQuery('.tabs .tab-content a').on('click', function(e)  {
					        var currentAttrValue = jQuery(this).attr('href');
					 
					        // Show/Hide Tabs
					        jQuery('.tabs ' + currentAttrValue).slideDown(400).siblings().slideUp(400);

					        // Change/remove current tab to active
					        jQuery(this).parent('li').addClass('active').siblings().removeClass('active');
					 
					        e.preventDefault();
					    });
					});
			    </script>

			</head>
			<body>

				<div class="tabs">
					<ul class="tab-links">
						<xsl:for-each select="cnn/region">
							<li>
								<a href="#{@id}"><xsl:value-of select='@id'/></a>
							</li>
						</xsl:for-each>
					</ul>

					<div class="tab-content">
						<xsl:for-each select="cnn/region">
						
							<div id="{@id}" class="tab">
								<xsl:for-each select="news">
									<h3><xsl:value-of select="title"/><br/></h3>
								
									<xsl:value-of select="datetime"/><br/>
									
									<xsl:if test="not(journalist_list)">Students<br/></xsl:if>
									
									<xsl:for-each select="journalist_list/journalist">
										<xsl:value-of select="."/><br/>
									</xsl:for-each>
									
									<br/>
									<xsl:value-of select="text"/>
									<br/>
									<br/>
									Images:
									<br/>
									<xsl:for-each select="photo_list/photo">
										<img src="{.}"></img><br/>
									</xsl:for-each>
									<br/>
									<xsl:if test="videourl">
									Video:
									<br/>
										<iframe width="100%" height="700" allowfullscreen="" src="{videourl}" scrolling="no"></iframe>
									<br/>
									</xsl:if>
									<br/>
									Storyhigh Light<br/>
									<xsl:for-each select="storyhighlights_list/storyhighlight">
										<xsl:value-of select="."/><br/>
									</xsl:for-each>
								</xsl:for-each>
							</div>

						</xsl:for-each>
					</div>

				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>			