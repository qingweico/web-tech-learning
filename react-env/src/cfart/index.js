import React from 'react';
import {Typography, Paper, Grid} from '@material-ui/core';

import {Toolbox} from './components/Toolbox';
import {SettingsPanel} from './components/SettingsPanel';
import {Topbar} from './components/Topbar';

import {Container} from './components/user/Container';
import {Button} from './components/user/Button';
import {Card} from './components/user/Card';
import {Text} from './components/user/Text';
import {Editor, Frame} from "@craftjs/core";

export default function CfartApp() {
    return (<div style={{margin: "0 auto", width: "800px"}}>
            <Typography variant="h5" align="center">A super simple page editor</Typography>
            <Editor resolver={{Card, Button, Text, Container}}>
                <Grid container spacing={3} style={{paddingTop: "10px"}}>
                    <Topbar/>
                    <Grid item xs>
                        <Frame>
                            <Container padding={5} background="#eee" canvas>
                                <Card/>
                            </Container>
                            <Button size="small" variant="outlined">Click</Button>
                            <Text size="small" text="Hi world!"/>
                            <Container padding={6} background="#999" canvas>
                                <Text size="small" text="It's me again!"/>
                            </Container>
                        </Frame>
                    </Grid>
                    <Grid item xs={3}>
                        <Paper>
                            <Toolbox/>
                            <SettingsPanel/>
                        </Paper>
                    </Grid>
                </Grid>
            </Editor>
        </div>);
}
